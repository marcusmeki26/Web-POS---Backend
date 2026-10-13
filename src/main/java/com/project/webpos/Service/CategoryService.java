package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.DTO.CategoryCountResponse;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  /**
   * Gets all the record inside {@code Category} entity
   * @return an object of {@code List<Category>}
   */
  public List<Category> getCategories(){
    return categoryRepository.findAll();
  }
  
  /**
   * Performs a checking if the category exist without returning anything
   * @param category the value of category to search
   */
  public void isCategoryExisting(String category){
    categoryRepository.findById(category)
              .orElseThrow(() -> new ResourceNotFoundException("INVALID_CATEGORY", "category does not exist"));
  }

   /**
   * Returns an object of {@code Category} based on its id
   * @param category the id of {@code Category} to be searched
   * @return an object of {@code Cateogry}
   */
  public Category getCategory(String category){
    return categoryRepository.findById(category)
              .orElseThrow(() -> new ResourceNotFoundException("INVALID_CATEGORY", "category does not exist"));
  }

  public List<CategoryCountResponse> getCategoriesCount() {
    return categoryRepository.getCategoriesCount();
  }
}
