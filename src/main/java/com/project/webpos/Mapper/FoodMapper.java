package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.DTO.FoodResponse;

@Mapper(componentModel = "spring")
public interface FoodMapper {
  @Mapping(source = "category.code", target = "categoryType")
  FoodResponse toResponse(Food foodList);

  List<FoodResponse> toResponseList(List<Food> foodList);
}
