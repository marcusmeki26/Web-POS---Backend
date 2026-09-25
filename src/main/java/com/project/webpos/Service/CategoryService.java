package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.DTO.CategoryCountResponse;
import com.project.webpos.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> getCategories(){
    return categoryRepository.findAll();
  }
  
  public boolean isCategoryExisting(String category){
    categoryRepository.findById(category)
              .orElseThrow(() -> new RuntimeException("Not Found"));

    return true;
  }

  public Category getCategoryInit(String category){
    return categoryRepository.findById(category)
              .orElseThrow(() -> new RuntimeException("Not Found"));
  }

  public List<CategoryCountResponse> getCategoriesCount() {
    return categoryRepository.getCategoriesCount();
  }
}
