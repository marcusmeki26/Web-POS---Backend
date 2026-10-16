package com.project.webpos.Controller.v1;

import java.util.List;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/v1/category")
public class CategoryController {
  private final CategoryService categoryService;
  private final ProductManagementService productManagementService;

  @GetMapping
  public ResponseEntity<List<CategoryResponse>> getCategories(){
    return ResponseEntity.ok().body(categoryService.getCategories());
  }
  
  @GetMapping("/count")
  public ResponseEntity<List<CategoryCountResponse>> getCategoriesCount(){
    return ResponseEntity.ok(categoryService.getCategoriesCount());
  }

  @PostMapping 
  public ResponseEntity<CategoryResponse> postCategory(
    @RequestBody CategoryPostRequest postRequest
  ){
    return ResponseEntity.ok().body(categoryService.postCategory(postRequest));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CategoryResponse> patchCategory(
    @PathVariable Integer id,
    @RequestBody CategoryPatchRequest patchRequest
  ){
    return ResponseEntity.ok().body(categoryService.patchCategory(id, patchRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(
    @PathVariable Integer id
  ){
    productManagementService.deleteCategory(id);
    return ResponseEntity.ok().build();
  }
}
