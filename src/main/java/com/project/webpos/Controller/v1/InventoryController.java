package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.InventorySummaryRequest;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping 
  public PagedResponse<List<InventorySummaryRequest>> getInventorySummary(
    @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
  ){
    return inventoryService.getInventorySummary(pageable);
  }
}
