package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class FoodPatchRequest {
  private JsonNullable<String> foodName = JsonNullable.undefined();
  private JsonNullable<String> shortFoodDesc = JsonNullable.undefined();
  private JsonNullable<BigDecimal> price = JsonNullable.undefined();
  private JsonNullable<Integer> quantity = JsonNullable.undefined();
  private JsonNullable<String> category = JsonNullable.undefined();
  private JsonNullable<Boolean> withVariant = JsonNullable.undefined();
  @Valid 
  private JsonNullable<List<ProductVariantPatchRequest>> variants = JsonNullable.undefined();
}
