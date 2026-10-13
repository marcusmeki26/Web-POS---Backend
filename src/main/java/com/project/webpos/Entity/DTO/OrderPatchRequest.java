package com.project.webpos.Entity.DTO;

import org.openapitools.jackson.nullable.JsonNullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPatchRequest {
  private JsonNullable<Integer> quantity = JsonNullable.undefined();
  private JsonNullable<Integer> tableNo = JsonNullable.undefined();
  private JsonNullable<Integer> orderType = JsonNullable.undefined();
  private JsonNullable<Boolean> placed = JsonNullable.undefined();
}
