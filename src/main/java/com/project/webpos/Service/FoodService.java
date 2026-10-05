package com.project.webpos.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.DTO.FoodCreateRequest;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Mapper.FoodMapper;
import com.project.webpos.Record.FoodImageOnly;
import com.project.webpos.Repository.FoodRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {
  private final FoodRepository foodRepository;
  private final CategoryService categoryService;
  private final ProductVariantService productVariantService;
  private final InventoryService inventoryService;
  private final FoodMapper foodMapper;
  private final FileStorageService fileStorageService;

  public List<FoodResponse> getFoodList() {
    List<Food> foodList = foodRepository.findAll();
    List<FoodResponse> foodResponseList = new ArrayList<>();
    for(Food food : foodList){
      if(food.getWithVariant()){
        foodResponseList.add(foodMapper.toResponse(food));
      }else{
        foodResponseList.add(foodMapper.toResponseWithoutVariant(food));
      }
    }
    return foodResponseList;
  }

  /**
   * Returns {@code Food} object based on the specified food id 
   * @param id the id of the food to be searched
   * @return an object of {@code Food}
   */
  public Food getFoodById(Long id){
    return foodRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("No existing food"));
  }

  /**
   * Returns a type of {@code FoodResponse} that filters based on its category.
   * @param categoryType The category type of {@code Category} entity
   * @return a {@code List<FoodResponse>}
   */
  public List<FoodResponse> getFoodByCategory(String categoryType) {
    if(categoryService.isCategoryExisting(categoryType)){
      List<Food> foods = foodRepository.findByCategory_code(categoryType);
      List<FoodResponse> foodResponseList = new ArrayList<>();
      for(Food food : foods){
        if(food.getWithVariant()){
          foodResponseList.add(foodMapper.toResponse(food));
        }else{
          foodResponseList.add(foodMapper.toResponseWithoutVariant(food));
        }
      }

      return foodResponseList;
    }

    // Move this to global exception handler
    throw new RuntimeException("There was an error during fetching of menu.");
  }

  /**
   * Returns the image of a {@code Food} entity based on its id
   * @param foodId the id of a {@code Food} entity to be searched
   * @return
   */
  public ResponseEntity<Resource> getImage(Long foodId){
    String imageName = getImageByFoodId(foodId);
    Path path = fileStorageService.getPath();
    try{
      Path imagePath = path.resolve(imageName).normalize();
      Resource resource = new UrlResource(imagePath.toUri());
      // Gets the type of content and appends it to the header
      String contentType = Files.probeContentType(imagePath);
      if(contentType == null)
        contentType = "application/octet-stream";
      if(resource.exists() || resource.isReadable()){
          return ResponseEntity.ok()
                  .contentType(MediaType.parseMediaType(contentType))
                  .body(resource);
      }
      return ResponseEntity.notFound().build();
    }catch(RuntimeException | IOException e){
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }

  public String getImageByFoodId(Long foodId){
    FoodImageOnly foodImageOnly = foodRepository.findProjectedById(foodId)
                                    .orElseThrow(() -> new RuntimeException("No existing food"));

    return foodImageOnly.imageName();
  }

  @Transactional
  public void setFood(FoodCreateRequest request, MultipartFile file){
    if(!request.getWithVariant() && request.getPrice() == null)
      throw new RuntimeException("price cannot be null");

    if(!request.getWithVariant() && request.getQuantity() == null)
      throw new RuntimeException("quantity cannot be null");

    Food food = new Food();
    food.setFoodName(request.getFoodName());
    food.setShortFoodDesc(request.getShortFoodDesc());
    food.setPrice(request.getPrice());
    food.setWithVariant(request.getWithVariant());
    food.setImageName(fileStorageService.saveFile(file));

    Category category = categoryService.getCategory(request.getCategory());
    food.setCategory(category);

    Food savedFood = foodRepository.save(food);

    if(request.getVariants().size() > 0){
      request.getVariants().forEach(variant -> {
        inventoryService.setInventory(savedFood, variant.getSizeQuantity(), productVariantService.setProductVariant(savedFood, variant));
      });
    }else{
      inventoryService.setInventory(savedFood, request.getQuantity());
    }
  }

  /**
   * Updates food detail based on food id
   * @param patchRequest An object of {@code patchRequest}
   * @param id The id of {@code Table} entity to be updated
   * @return None
   */
  @Transactional 
  public void patchFoodById(FoodPatchRequest patchRequest, Long id){
    Food food = getFoodById(id);
    food.setFoodName(patchRequest.getName());
    food.setShortFoodDesc(patchRequest.getShortDesc());
    food.setWithVariant(patchRequest.getWithVariant());
    food.setShortFoodDesc(patchRequest.getShortDesc());
    
    if(food.getWithVariant()){
      food.setPrice(BigDecimal.ZERO);
      productVariantService.patchProdVarByFoodId(food, patchRequest.getVariants());
    }
    else{
      food.setPrice(patchRequest.getPrice());
      inventoryService.patchInventoryByFoodIdWithoutVariant(food, patchRequest.getQuantity());
    }

    Category category = categoryService.getCategory(patchRequest.getCategory());
    food.setCategory(category);

    foodRepository.save(food);
  }

  /**
   * Deletes a record based on the specified {@code Food} id and {@code Size} id. Also deletes record from {@code Inventory} entity & {@code ProductVariant} entity
   * @param foodId the value of {@code Food} id to be searched
   * @param sizeId the value of {@code Size} id to be searched
   * @return None
   */
  public void deleteByFoodIdAndSizeId(Long foodId, Integer sizeId){
    ProductVariant productVariant = productVariantService.getProductVariantByFoodAndSize(foodId, sizeId);
    
    inventoryService.deleteByFoodIdAndProductVariantId(foodId, productVariant.getId());
    productVariantService.deleteProductVariant(productVariant);
  }

  /**
   * Deletes a record based on the specified id of {@code Food}
   * @param foodId the id of {@code Food} entity to delete
   * @return None
   */
  public void deleteFood(Long foodId){
    Food food = getFoodById(foodId);
    // Check if working ang implementation :REMOVE-COMMENT
    if(food.getWithVariant()){
      productVariantService.deleteByFoodIdAndInventory(foodId);
    }else{
      inventoryService.deleteByFoodId(foodId);
    }

    foodRepository.delete(food);
  }
}
