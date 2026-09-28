package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.Size;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Mapper.FoodMapper;
import com.project.webpos.Repository.FoodRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {
  private final FoodRepository foodRepository;
  private final CategoryService categoryService;
  private final ProductVariantService productVariantService;
  private final SizeService sizeService;
  private final InventoryService inventoryService;
  private final FoodMapper foodMapper;

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

  // Used for initializing menu (not permanent)
  public void setFoodInit() {
    Food food1 = new Food();
    food1.setFoodName("Overloaded French Fries");
    food1.setShortFoodDesc("Overloaded FF");
    food1.setPrice(BigDecimal.valueOf(85.00));
    food1.setImagePath("");

    Category category = categoryService.getCategoryInit("APPETIZER");
    food1.setCategory(category);
    foodRepository.save(food1);

    inventoryService.setInventoryInit(food1, 3);

    Food food2 = new Food();
    food2.setFoodName("Coke");
    food2.setShortFoodDesc("Coke");
    food2.setWithVariant(true);
    food2.setImagePath("");

    if(food2.getWithVariant())
      food2.setPrice(BigDecimal.valueOf(0));

    category = categoryService.getCategoryInit("BEVERAGE");
    food2.setCategory(category);
    foodRepository.save(food2);
    
    Size size1 = new Size();
    size1 = sizeService.getSize(1);
    Size size2 = new Size();
    size2 = sizeService.getSize(2);
    Size size3 = new Size();
    size3 = sizeService.getSize(3);
    productVariantService.postProdVarInit(food2, size1, 25.00);
    productVariantService.postProdVarInit(food2, size2, 50.00);
    productVariantService.postProdVarInit(food2, size3, 70.00);

    // Can do this same logic on how to save into Inventory table
    if(food2.getWithVariant()){
      // The quantity should be on a list
      List<Integer> qtyList = List.of(15, 3, 33);

      List<ProductVariant> prodVariantList = productVariantService.getProdVarByFoodId(food2.getId());
      
      int index = 0;
      for (ProductVariant prodVariant : prodVariantList) {
        inventoryService.setInventoryInit(food2, prodVariant, qtyList.get(index));
        index++;
      }
    }

    Food food3 = new Food();
    food3.setFoodName("Panna Cotta");
    food3.setShortFoodDesc("Panna Cotta");
    food3.setPrice(BigDecimal.valueOf(490.00));
    food3.setImagePath("");
    
    category = categoryService.getCategoryInit("DESSERT");
    food3.setCategory(category);
    foodRepository.save(food3);
    
    inventoryService.setInventoryInit(food3, 49);

    Food food4 = new Food();
    food4.setFoodName("Sweet Chili Salmon");
    food4.setShortFoodDesc("SC Salmon");
    food4.setPrice(BigDecimal.valueOf(350.00));
    food4.setImagePath("");
    
    category = categoryService.getCategoryInit("MAIN DISH");
    food4.setCategory(category);
    foodRepository.save(food4);

    inventoryService.setInventoryInit(food4, 12);

    Food food5 = new Food();
    food5.setFoodName("Shrimp Cocktail");
    food5.setShortFoodDesc("Shrimp Cocktail");
    food5.setPrice(BigDecimal.valueOf(599.00));
    food5.setImagePath("");
    
    category = categoryService.getCategoryInit("APPETIZER");
    food5.setCategory(category);
    foodRepository.save(food5);

    inventoryService.setInventoryInit(food5, 0);
    // Since it will be a JSON object
    // Get the object for prod variant then loop from there
    // if(food2.getWithVariant()){
    //   Integer sumQty = 0;
    //   for (ProductVariant prodVarirant: productVariantService.getProdVarByFoodId(food2.getId())) {
    //     sumQty += prodVarirant.getQuantity();
    //   }

    //   food2.setQuantity(sumQty);
    // }

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

    Category category = categoryService.getCategoryInit(patchRequest.getCategory());
    food.setCategory(category);

    foodRepository.save(food);
  }

  /**
   * Deletes a record based on the specified {@code Food} id and {@code Size} id. Also deletes record from {@code Inventory} entity & {@code ProductVariant} entity
   * @param foodId the value of {@code Food} id to be searched
   * @param sizeId the value of {@code Size} id to be searched
   */
  public void deleteByFoodIdAndSizeId(Long foodId, Integer sizeId){
    ProductVariant productVariant = productVariantService.getProductVariantByFoodAndSize(foodId, sizeId);
    if(productVariant == null)
      throw new RuntimeException("No existing product variant");

    Inventory inventory = inventoryService.getInventoryByFoodIdAndProductVariantId(foodId, productVariant.getId());
    if(inventory == null)
      throw new RuntimeException("No existing inventory");

    inventoryService.deleteInventory(inventory);
    productVariantService.deteleProductVariant(productVariant);
  }
}
