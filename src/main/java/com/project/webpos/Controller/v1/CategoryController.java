package com.project.webpos.Controller.v1;

import java.util.List;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.CategoryCountResponse;
import com.project.webpos.Entity.DTO.CategoryPatchRequest;
import com.project.webpos.Entity.DTO.CategoryPostRequest;
import com.project.webpos.Entity.DTO.CategoryResponse;
import com.project.webpos.Service.CategoryService;
import com.project.webpos.Service.Shared.ProductManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/v1/category")
@Tag(name = "Category", description = "Endpoints for managing category")
public class CategoryController {
  private final CategoryService categoryService;
  private final ProductManagementService productManagementService;

  @GetMapping
  @Operation(
    summary = "Retrieves all category",
    description = "Fetches all active & undeleted category"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all category")
  public ResponseEntity<List<CategoryResponse>> getCategories(){
    return ResponseEntity.ok().body(categoryService.getCategories());
  }
  
  @GetMapping("/count")
  @Operation(
    summary = "Gets the count of food by based on its category",
    description = "Returns the number of food that is on each category together with the category name"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public ResponseEntity<List<CategoryCountResponse>> getCategoriesCount(){
    return ResponseEntity.ok(categoryService.getCategoriesCount());
  }

  @PostMapping 
  @Operation(
    summary = "Adds a new category",
    description = "Adds the new category to the Category table. Returns the id & category name"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully created"),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<CategoryResponse> postCategory(
    @RequestBody CategoryPostRequest postRequest
  ){
    return ResponseEntity.ok().body(categoryService.postCategory(postRequest));
  }

  @PatchMapping("/{id}")
  @Operation(
    summary = "Partially modifies a category",
    description = "Updates only the specified field of the target category. Omitted fields are remain untouched"
  )
  @ApiResponse(responseCode = "200", description = "Successfully updated")
  public ResponseEntity<CategoryResponse> patchCategory(
    @Parameter(description = "the id of the target category to modify", required = true, example = "APPETIZER")
    @PathVariable Integer id,
    @RequestBody CategoryPatchRequest patchRequest
  ){
    return ResponseEntity.ok().body(categoryService.patchCategory(id, patchRequest));
  }

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Deletes a category",
    description = "Soft deletes a category. This is currently irreversible"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Successfully deleted"),
    @ApiResponse(responseCode = "404", description = "Category does not exist"),
    @ApiResponse(responseCode = "409", description = "Unable to delete since there are existing food that uses the target category")
  })
  public ResponseEntity<Void> deleteCategory(
    @Parameter(description = "the id of the target category", required = true)
    @PathVariable Integer id
  ){
    productManagementService.deleteCategory(id);
    return ResponseEntity.ok().build();
  }
}
