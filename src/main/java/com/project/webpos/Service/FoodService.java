package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.Size;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Mapper.FoodMapper;
import com.project.webpos.Repository.FoodRepository;

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
    return foodMapper.toResponseList(foodList);
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

  public List<FoodResponse> getFoodByCategory(String categoryType) {
    if(categoryService.getCategory(categoryType)){
      List<Food> foods = foodRepository.findByCategory_code(categoryType);
      
      return foodMapper.toResponseList(foods);
    }

    // Move this to global exception handler
    throw new RuntimeException("There was an error during fetching of menu.");
  }
}
