package com.project.webpos.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Entity.DTO.FoodCreateRequest;
import com.project.webpos.Entity.DTO.FoodCreateResponse;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodPatchResponse;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Entity.DTO.ProductVariantPatchRequest;
import com.project.webpos.Exception.InvalidRequestException;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.Mapper.FoodMapper;
import com.project.webpos.Record.FoodImageOnly;
import com.project.webpos.Repository.FoodRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor 
@Slf4j
public class FoodService {
  private final FoodRepository foodRepository;
  private final ProductVariantService productVariantService;
  private final InventoryService inventoryService;
  private final FoodMapper foodMapper;
  private final FileStorageService fileStorageService;

  /**
   * Retrieves all the record from {@code Food} entity that is active and not deleted
   * @return an object of {@code List<FoodResponse>}
   */
  public List<FoodResponse> getFoodList() {
    // List<Food> foodList = foodRepository.findAll();
    List<Food> foodList = foodRepository.findActiveAndNotDeleted();
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
    return foodRepository.findByIdAndIsDeletedIsFalseAndIsActiveIsTrue(id)
      .orElseThrow(() -> new ResourceNotFoundException("FOOD_NOT_EXIST", "food does not exist"));
  }

  /**
   * Returns a type of {@code FoodResponse} that filters based on its category.
   * @param categoryType The category type of {@code Category} entity
   * @return a {@code List<FoodResponse>}
   */
  public List<FoodResponse> getFoodByCategory(Integer id) {
    List<Food> foods = foodRepository.findCategoryAndActiveAndNotDeleted(id);
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

  /**
   * Retrieves the image path based on the specified food id
   * @param foodId the value of food id to search
   * @return a {@code String} of image name
   */
  public String getImageByFoodId(Long foodId){
    FoodImageOnly foodImageOnly = foodRepository.findProjectedById(foodId)
                                    .orElseThrow(() -> new ResourceNotFoundException("INVALID_FOOD", "food does not exist"));

    return foodImageOnly.imageName();
  }

  /**
   * Creates a new record of {@code Food} entity
   * @param request an object of {@code FoodCreateRequest}
   * @param file an object of {@code MultipartFile} that contains the image
   * @param category an object of {@code Category}
   */
  @Transactional
  public FoodCreateResponse setFood(FoodCreateRequest request, MultipartFile file, Category category){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

    if(!request.getWithVariant() && request.getPrice() == null)
      throw new InvalidRequestException("UNABLE_TO_PROCESS", "CREATION", "price is required");

    if(!request.getWithVariant() && request.getQuantity() == null)
      throw new InvalidRequestException("UNABLE_TO_PROCESS", "CREATION", "quantity is required");

    Food food = new Food();
    food.setIsActive(true);
    food.setIsDeleted(false);
    food.setFoodName(request.getFoodName());
    food.setShortFoodDesc(request.getShortFoodDesc());
    food.setPrice(request.getPrice());
    food.setWithVariant(request.getWithVariant());
    food.setImageName(fileStorageService.saveFile(file));

    // Category category = productManagementService.getCategory(request.getCategoryId());
    food.setCategory(category);

    Food savedFood = foodRepository.save(food);

    if(request.getVariants().size() > 0){
      request.getVariants().forEach(variant -> {
        inventoryService.setInventory(savedFood, variant.getSizeQuantity(), productVariantService.setProductVariant(savedFood, variant));
      });
    }else{
      inventoryService.setInventory(savedFood, request.getQuantity());
    }
    
    log.info("A new Food is added {} by User {}", savedFood.getId(), loggedUser.getId());
    return foodMapper.toCreateResponse(savedFood);
  }

  /**
   * Updates food detail based on food id
   * @param patchRequest An object of {@code patchRequest}
   * @param id The id of {@code Table} entity to be updated
   * @return None
   */
  @Transactional 
  public FoodPatchResponse patchFoodById(FoodPatchRequest patchRequest, Long id, Category category){
    if(patchRequestHasRecord(patchRequest)){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();
  
      Food food = getFoodById(id);
      foodMapper.patchFood(patchRequest, food);

      if(category != null)
        food.setCategory(category);
      
      if(food.getWithVariant() && !patchRequest.getVariants().isPresent())
        throw new InvalidRequestException("UNABLE_TO_PROCESS", "FOOD PATCH UPDATE", "variant is required if food is marked with variant");
  
      if(food.getWithVariant()){
        List<ProductVariantPatchRequest> prodVariants = new ArrayList<>();
        patchRequest.getVariants()
          .get()
          .stream()
          .forEach(variant -> {
            ProductVariantPatchRequest prodVariant = new ProductVariantPatchRequest();
            prodVariant.setSizeId(variant.getSizeId());
            if(variant.getSizePrice() != null)
              prodVariant.setSizePrice(variant.getSizePrice());
            if(variant.getSizeQuantity() != null)
              prodVariant.setSizeQuantity(variant.getSizeQuantity());
            prodVariant.setSizeOldId(variant.getSizeOldId());
            prodVariants.add(prodVariant);
          });
        food.setPrice(BigDecimal.ZERO);
        productVariantService.patchProdVarByFoodId(food, prodVariants);
      }else{
        if(patchRequest.getQuantity().isPresent())
          inventoryService.patchInventoryByFoodIdWithoutVariant(food, patchRequest.getQuantity().orElse(0));
      }
  
      log.info("A Food {} has been updated by User {}", food.getId(), loggedUser.getId());
      return foodMapper.toPatchResponse(foodRepository.save(food));
    }

    return new FoodPatchResponse();
  }

  /**
   * <b>NOTE: Does not delete {@code Food} entity</b> <br>
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
   * Soft deletes a record based on the specified id of {@code Food}
   * @param foodId the id of {@code Food} entity to delete
   * @return None
   */
  public void deleteFood(Long foodId){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

    Food food = getFoodById(foodId);
    food.setIsDeleted(true);
    food.setIsActive(false);
    food.setDeletedAt(LocalDateTime.now());
    foodRepository.save(food);
    log.info("Food {} is soft deleted by user {}", food.getId(), loggedUser.getId());
  }

  // =======================================================
  // UTILITIES
  // =======================================================
  private boolean patchRequestHasRecord(FoodPatchRequest request){
    if(request == null)
      return false;

    return (request.getFoodName().isPresent()
            || request.getShortFoodDesc().isPresent()
            || request.getCategory().isPresent()
            || request.getPrice().isPresent()
            || request.getQuantity().isPresent()
            || request.getWithVariant().isPresent()
            || request.getVariants().isPresent());
  }

  public boolean hasRecordByCategory(Category category){
    Food food = foodRepository.findFirstByCategory(category);

    return food == null;
  }
}
