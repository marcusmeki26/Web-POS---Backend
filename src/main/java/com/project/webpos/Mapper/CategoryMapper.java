package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

import com.project.webpos.Entity.DTO.CategoryPatchRequest;
import com.project.webpos.Entity.DTO.CategoryResponse;
import com.project.webpos.Entity.Category;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {
  CategoryResponse toResponse(Category category);
  List<CategoryResponse> toResponseList(List<Category> category);

  void patchCategory(CategoryPatchRequest patchRequest, @MappingTarget Category category);
  // This generic fallback unwraps ALL JsonNullable fields automatically
  default <T> T unwrap(JsonNullable<T> jsonNullable) {
      return jsonNullable == null ? null : jsonNullable.orElse(null); 
  }

  // This tells MapStruct to ONLY touch fields that the user intentionally provided
  @Condition
  default <T> boolean isPresent(JsonNullable<T> jsonNullable) {
      return jsonNullable != null && jsonNullable.isPresent();
  }
}
