package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.DTO.FoodResponse;

@Mapper(componentModel = "spring")
public interface FoodMapper {
  @Named("withVariantFoodMapping")
  @Mapping(source = "category.code", target = "categoryType")
  @Mapping(target = "quantity", ignore = true)
  FoodResponse toResponse(Food foodList);

  @IterableMapping(qualifiedByName = "withVariantFoodMapping")
  List<FoodResponse> toResponseList(List<Food> foodList);

  @Mapping(source = "category.code", target = "categoryType")
  @Mapping(source = "food", target = "quantity")
  @Mapping(target = "inventory", ignore = true)
  FoodResponse toResponseWithoutVariant(Food food);

  default Integer mapInventoryToQuantity(Food food){
    if (food.getInventory() == null || food.getInventory().isEmpty()) {
      return 0; 
    }
    
    return food.getInventory().stream()
            .filter(inv -> inv.getQuantity() != null) 
            .mapToInt(Inventory::getQuantity) 
            .sum();
  }
}
