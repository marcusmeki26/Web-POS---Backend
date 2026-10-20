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
import com.project.webpos.Entity.DTO.FoodCreateResponse;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodPatchResponse;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Exception.ErrorMessage;
import com.project.webpos.Service.FoodService;
import com.project.webpos.Service.Shared.ProductManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController
@RequestMapping("/v1/food")
@Tag(name = "Food", description = "Endpoints for managing food")
public class FoodController {
  private final FoodService foodService;
  private final ProductManagementService productManagementService;

  @GetMapping
  @Operation(summary = "Retrieves all food or retrieves food based on its category ID", description = "Fetches all active & undeleted food")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved food/s")
  public ResponseEntity<List<FoodResponse>> getFoodList(
    @Parameter(description = "Filter food by is category ID", required = false, example = "1")
    @RequestParam(name = "category", required = false) Integer id
  ){
    if(id != null)
      return ResponseEntity.ok(productManagementService.getFoodByCategory(id));

    return ResponseEntity.ok(foodService.getFoodList());
  }

  @GetMapping("/image/{foodId}")
  @Operation(summary = "Retrieves image by food ID", description = "Fetches the path and returned as a type of resource")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully retrieved image"),
    @ApiResponse(responseCode = "404", description = "Food ID not found")
  })
  public ResponseEntity<Resource> getImage(
    @Parameter(description = "ID of the food to retrieve its image", required = true, example = " 10000")
    @PathVariable Long foodId
  ){
    return foodService.getImage(foodId);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('OWNER')")
  @Operation(
    summary = "Create a new food", 
    description = "Adds the new food on the menu. Returns the food name & short description"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully created"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed on business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<FoodCreateResponse> setFood(
    @Valid @RequestPart("food") FoodCreateRequest foodCreateRequest,
    @RequestPart("image") MultipartFile file
  ){
    // foodService.setFoodInit();
    return ResponseEntity.ok().body(productManagementService.setFood(foodCreateRequest, file));
  }

  @PatchMapping("/{foodId}")
  @PreAuthorize("hasRole('OWNER')")
  @Operation(
    summary = "Partially modify a product", 
    description = "Updates only the specified fields of a target product. Omitted fields will remain untouched."
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully updated"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed on business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<FoodPatchResponse> patchFoodById(
    @Valid @RequestBody FoodPatchRequest patchRequest,
    @Parameter(description = "The ID of the target food to modify", required = true, example = "10000")
    @PathVariable Long foodId
  ){
    // foodService.patchFoodById(patchRequest, foodId);
    // return ResponseEntity.ok().build();
    return ResponseEntity.ok().body(productManagementService.patchFoodById(patchRequest, foodId));
  }

  @DeleteMapping("/{foodId}/{sizeId}")
  @PreAuthorize("hasRole('OWNER')")
  @Operation(
    summary = "Deletes a food with variant",
    description = "Soft deletes a food from the database. This is currently irreversible"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Food deleted successfully. No content returned"),
    @ApiResponse(responseCode = "404", description = "Food or size not found")
  })
  public ResponseEntity<Void> deleteByFoodIdAndSizeId(
    @Parameter(description = "The ID of the target food to delete", required = true, example = "10000")
    @PathVariable Long foodId,
    @Parameter(description = "The size ID of the target food to delete", required = true, example = "1")
    @PathVariable Integer sizeId
  ){
    foodService.deleteByFoodIdAndSizeId(foodId, sizeId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{foodId}")
  @PreAuthorize("hasRole('OWNER')")
  @Operation(
    summary = "Deletes a food without variant",
    description = "Soft deletes a food from the database. This if currently irreversible"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Food deleted successfully. No content returned"),
    @ApiResponse(responseCode = "404", description = "Food not found")
  })
  public ResponseEntity<Void> deleteFood(
    @Parameter(description = "The ID of the target food to delete", required = true, example = "10000")
    @PathVariable Long foodId
  ){
    foodService.deleteFood(foodId);
    return ResponseEntity.noContent().build();
  }
}
