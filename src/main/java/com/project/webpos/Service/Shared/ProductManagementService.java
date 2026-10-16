package com.project.webpos.Service.Shared;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.DTO.FoodCreateRequest;
import com.project.webpos.Entity.DTO.FoodCreateResponse;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodPatchResponse;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Exception.ConflictException;
import com.project.webpos.Service.CategoryService;
import com.project.webpos.Service.FoodService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ProductManagementService {
  private final FoodService foodService;
  private final CategoryService categoryService;

  // =======================================================
  // GOES TO FOOD SERVICE
  // =======================================================
  public List<FoodResponse> getFoodByCategory(Integer id){
    categoryService.isCategoryExisting(id);

    return foodService.getFoodByCategory(id);
  }

  public FoodCreateResponse setFood(FoodCreateRequest postRequest, MultipartFile file){
    Category category = categoryService.getCategory(postRequest.getCategoryId());

    return foodService.setFood(postRequest, file, category);
  }

  public FoodPatchResponse patchFoodById(FoodPatchRequest patchRequest, Long id){
    Category category = null;

    if(patchRequest.getCategory().isPresent())
      category = categoryService.getCategory(patchRequest.getCategory().get().getId());
    
    return foodService.patchFoodById(patchRequest, id, category);
  }

  // =======================================================
  // GOES TO CATEGORY SERVICE
  // =======================================================
  public void deleteCategory(Integer id){
    Category category = categoryService.getCategory(id);

    if(foodService.hasRecordByCategory(category)){
      categoryService.deleteCategory(category);
    }

    throw new ConflictException("DELETE_VIOLATION", "deletion of category", "Cannot delete this category as this is being used by an active food. Please update the food first.");
  }
}
