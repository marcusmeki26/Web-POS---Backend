package com.project.webpos.Entity.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
public class InventorySummaryRequest {
  private Long foodId;
  private String foodName;
  private Integer prodVarId;
  private String prodVarName;
  private Integer quantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
