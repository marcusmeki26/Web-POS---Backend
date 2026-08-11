package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Service.FoodService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/food")
public class FoodController {
  private FoodService foodService;

  @GetMapping
  public ResponseEntity<List<FoodResponse>> getFoodList(
    @RequestParam(name = "category", required = false) String categoryType
  ){
    if(categoryType != null && !categoryType.isBlank())
      return ResponseEntity.ok(foodService.getFoodByCategory(categoryType));

    return ResponseEntity.ok(foodService.getFoodList());
  }

  @PostMapping
  public void setFood(){
    foodService.setFoodInit();
  }
}
