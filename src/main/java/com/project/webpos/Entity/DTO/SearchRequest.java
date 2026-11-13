package com.project.webpos.Entity.DTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SearchRequest(
  @NotNull(message = "filter/s are required")
  List<@Valid FilterRequest> filters
) {}
