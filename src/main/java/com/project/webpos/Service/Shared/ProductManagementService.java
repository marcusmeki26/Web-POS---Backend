package com.project.webpos.Service.Shared;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.DTO.FoodCreateRequest;
import com.project.webpos.Entity.DTO.FoodCreateResponse;
import com.project.webpos.Entity.DTO.FoodPatchRequest;
import com.project.webpos.Entity.DTO.FoodPatchResponse;
import com.project.webpos.Entity.DTO.FoodResponse;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Exception.ConflictException;
import com.project.webpos.Exception.StockIsEmptyException;
import com.project.webpos.Service.CategoryService;
import com.project.webpos.Service.FoodService;
import com.project.webpos.Service.InventoryService;
import com.project.webpos.Service.ProductVariantService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ProductManagementService {
  private final FoodService foodService;
  private final CategoryService categoryService;
  private final InventoryService inventoryService;
  private final ProductVariantService productVariantService;

  // =======================================================
  // GOES TO FOOD SERVICE
  // =======================================================
  public PagedResponse<FoodResponse> getFoodByCategory(Integer id, Pageable pageable){
    categoryService.isCategoryExisting(id);

    return foodService.getFoodByCategory(id, pageable);
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
      return;
    }

    throw new ConflictException("DELETE_VIOLATION", "deletion of category", "Cannot delete this category as this is being used by an active food. Please update the food first.");
  }

  // =======================================================
  // INVENTORY BUSINESS LOGIC
  // =======================================================
  /**
   * Performs a checking if there are still available stock
   * @param product an object of {@code OrderList} that will be checked
   */
  public void checkProductAvailability(Long foodId, Integer sizeId, Integer quantity){
    Inventory inventory = new Inventory();

    if(sizeId == null)
      inventory = inventoryService.getInventoryWithoutVariantWithLock(foodId);
    else{
      ProductVariant productVariant = productVariantService.getProductVariantByFoodAndSize(foodId, sizeId);
      inventory = inventoryService.getInventoryWithVariantWithLock(foodId, productVariant.getId());
    }

    if(inventory.getQuantity() < quantity){
      throw new StockIsEmptyException("INVENTORY_OUT_OF_STOCK", "cannot add more quantity to food " + foodId + " stock is empty");  
    }

    inventory.setQuantity(inventory.getQuantity() - quantity);
    inventoryService.updateInventory(inventory);
  }

  /**
   * Reverts the quantity count of the specified product
   * @param product an object of {@code OrderList}
   */
  public void revertQuantityCount(OrderList product){
    Inventory inventory = new Inventory();

    if(product.getSizeId() == null)
      inventory = inventoryService.getInventoryWithoutVariantWithLock(product.getNumber());
    else{
      ProductVariant productVariant = productVariantService.getProductVariantByFoodAndSize(product.getNumber(), product.getSizeId());
      inventory = inventoryService.getInventoryWithVariantWithLock(product.getNumber(), productVariant.getId());
    }
    
    inventory.setQuantity(inventory.getQuantity() + product.getQuantity());
    inventoryService.updateInventory(inventory);
  }
}
