package com.project.webpos.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Entity.DTO.CategoryCountResponse;
import com.project.webpos.Entity.DTO.CategoryPatchRequest;
import com.project.webpos.Entity.DTO.CategoryPostRequest;
import com.project.webpos.Entity.DTO.CategoryResponse;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.Mapper.CategoryMapper;
import com.project.webpos.Repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor 
@Slf4j
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  /**
   * Gets all the record inside {@code Category} entity
   * @return an object of {@code List<Category>}
   */
  public List<CategoryResponse> getCategories(){
    return categoryMapper.toResponseList(categoryRepository.findAllByIsActiveIsTrueAndIsDeletedIsFalse());
  }
  
  /**
   * Performs a checking if the category exist without returning anything
   * @param id the value of category to search
   */
  public void isCategoryExisting(Integer id){
    categoryRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("INVALID_CATEGORY", "category does not exist"));
  }

   /**
   * Returns an object of {@code Category} based on its id
   * @param id the id of {@code Category} to be searched
   * @return an object of {@code Cateogry}
   */
  public Category getCategory(Integer id){
    return categoryRepository.findByIdAndIsActiveIsTrueAndIsDeletedIsFalse(id)
              .orElseThrow(() -> new ResourceNotFoundException("INVALID_CATEGORY", "category does not exist"));
  }

  /**
   * Gets the count of category
   * @return an object of {@code List<CategoryCountResponse>>}
   */
  public List<CategoryCountResponse> getCategoriesCount() {
    return categoryRepository.getCategoriesCount();
  }

  /**
   * 
   * @param postRequest
   * @return
   */
  public CategoryResponse postCategory(CategoryPostRequest postRequest){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

    Category category = new Category();

    category.setCode(postRequest.getCode());
    category.setIsActive(true);
    category.setIsDeleted(false);

    CategoryResponse categoryResponse = categoryMapper.toResponse(categoryRepository.save(category));
    log.info("A category has been added {} by User {}", categoryResponse.getId() ,loggedUser.getId());
    return categoryResponse;
  }

  /**
   * Peforms a patch on the {@code Category} entity
   * @param code a string value of the old code
   * @param patchRequest an object of {@code CategoryPatchRequest}
   * @return an object of {@code CategoryResponse}
   */
  public CategoryResponse patchCategory(Integer id, CategoryPatchRequest patchRequest){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

    Category category = getCategory(id);
    categoryMapper.patchCategory(patchRequest, category);
    log.info("A category {} has been updated by User {}", id, loggedUser.getId());
    return categoryMapper.toResponse(categoryRepository.save(category));
  }

  /**
   * Soft deletes the specific {@code Category} based on its id. <br>
   * It will require that all referenced {@code Food} should be soft deleted to
   * @param id the id of {@code Category} to be deleted
   */
  public void deleteCategory(Category category){
    category.setIsDeleted(true);
    category.setDeletedAt(LocalDateTime.now());
    categoryRepository.save(category);
  }
}
