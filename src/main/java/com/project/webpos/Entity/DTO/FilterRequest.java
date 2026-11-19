package com.project.webpos.Entity.DTO;

import com.project.webpos.Enum.FilterOperator;

import jakarta.validation.constraints.NotBlank;

public record FilterRequest(
  @NotBlank(message = "field is required")
  String field,
  FilterOperator operator,
  @NotBlank(message = "value is required")
  String value
) {}
