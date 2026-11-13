package com.project.webpos.Entity.DTO;

import com.project.webpos.Enum.FilterOperator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilterRequest(
  @NotBlank(message = "field is required")
  String field,
  FilterOperator operator,
  @NotNull(message = "value is required")
  Object value
) {}
