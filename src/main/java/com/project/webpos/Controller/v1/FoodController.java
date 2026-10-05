package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.webpos.Entity.DTO.FoodCreateRequest;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Service.FoodService;

import jakarta.validation.Valid;
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

  @GetMapping("/image/{foodId}")
  public ResponseEntity<Resource> getImage(
    @PathVariable Long foodId
  ){
    return foodService.getImage(foodId);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('OWNER')")
  public ResponseEntity<Void> setFood(
    @Valid @RequestPart("food") FoodCreateRequest foodCreateRequest,
    @RequestPart("image") MultipartFile file
  ){
    // foodService.setFoodInit();
    foodService.setFood(foodCreateRequest, file);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{foodId}")
  @PreAuthorize("hasRole('OWNER')")
  public ResponseEntity<Void> patchFoodById(
    @Valid @RequestBody FoodPatchRequest patchRequest,
    @PathVariable Long foodId
  ){
    foodService.patchFoodById(patchRequest, foodId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{foodId}/{sizeId}")
  @PreAuthorize("hasRole('OWNER')")
  public ResponseEntity<Void> deleteByFoodIdAndSizeId(
    @PathVariable Long foodId,
    @PathVariable Integer sizeId
  ){
    foodService.deleteByFoodIdAndSizeId(foodId, sizeId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{foodId}")
  @PreAuthorize("hasRole('OWNER')")
  public ResponseEntity<Void> deleteFood(
    @PathVariable Long foodId
  ){
    foodService.deleteFood(foodId);
    return ResponseEntity.ok().build();
  }
}
