package com.project.webpos.Entity.DTO;

import org.openapitools.jackson.nullable.JsonNullable;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class SizePatchRequest {
  JsonNullable<String> sizeName = JsonNullable.undefined();
}
