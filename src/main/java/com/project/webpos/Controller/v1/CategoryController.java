package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.DTO.CategoryCountResponse;
import com.project.webpos.Service.CategoryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/category")
public class CategoryController {
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> getCategories(){
    return ResponseEntity.ok(categoryService.getCategories());
  }
  
  @GetMapping("/count")
  public ResponseEntity<List<CategoryCountResponse>> getCategoriesCount(){
    return ResponseEntity.ok(categoryService.getCategoriesCount());
  }
}
