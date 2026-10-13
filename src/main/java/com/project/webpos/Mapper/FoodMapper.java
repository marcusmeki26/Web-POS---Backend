package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Condition;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.DTO.FoodCreateResponse;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodPatchResponse;
import com.project.webpos.Entity.DTO.FoodResponse;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
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

  FoodCreateResponse toCreateResponse(Food food);
  FoodPatchResponse toPatchResponse(Food food);

  @Mapping(target = "category", source = "category", qualifiedByName = "mapCategory")
  void patchFood(FoodPatchRequest patchRequest, @MappingTarget Food food);
  // This generic fallback unwraps ALL JsonNullable fields automatically
  default <T> T unwrap(JsonNullable<T> jsonNullable) {
      return jsonNullable == null ? null : jsonNullable.orElse(null); 
  }

  // This tells MapStruct to ONLY touch fields that the user intentionally provided
  @Condition
  default <T> boolean isPresent(JsonNullable<T> jsonNullable) {
      return jsonNullable != null && jsonNullable.isPresent();
  }

  @Named("mapCategory")
  default Category category(JsonNullable<String> categoryCode){
    if(!categoryCode.isPresent() || categoryCode == null)
      return null;

    String code = categoryCode.orElse(null);
    if(code == null)
      return null;

    Category category = new Category();
    category.setCode(code); 
    return category;
  }

}
