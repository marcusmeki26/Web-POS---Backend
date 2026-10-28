package com.project.webpos.Service;

import org.openapitools.jackson.nullable.JsonNullable;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.JWT.JWTCheckerPerRequest;
import com.project.webpos.Logging.LoggerFormat;
import com.project.webpos.Repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  // Used for initializing menu (not permanent)
  public void setInventoryInit(Food food, Integer qty){
    Inventory inventory = new Inventory();

    inventory.setFood(food);
    inventory.setQuantity(qty);

    inventoryRepository.save(inventory);
  }
  
  /**
   * Inserts new record into {@code Inventory} for {@code Food} without variant
   * @param food
   * @param quantity
   */
  public void setInventory(Food food, Integer quantity){
    Inventory inventory = new Inventory();
    inventory.setFood(food);
    inventory.setQuantity(quantity);
    inventoryRepository.save(inventory);
  }

  /**
   * Inserts new record into {@code Inventory} for {@code Food} with variant
   * @param food
   * @param quantity
   */
  public void setInventory(Food food, Integer quantity, ProductVariant productVariant){
    Inventory inventory = new Inventory();
    inventory.setFood(food);
    inventory.setProductVariant(productVariant);
    inventory.setQuantity(quantity);
    inventoryRepository.save(inventory);
  }

  /**
   * Returns {@code Inventory} object based on the specified food and product variant
   * @param food an object of {@code Food} to be searched
   * @param prodVariant an object of {@code ProductVariant} to be searched
   * @return
   */
  public Inventory getInventoryByFoodAndProductVariant(Food food, ProductVariant prodVariant){
    return inventoryRepository.findByFoodAndProductVariant(food, prodVariant);
  }

  /**
   * Returns {@code Inventory} object based on the specified food
   * @param food an object of {@code Food} to be searched
   * @return
   */
  public Inventory getInventoryByFood(Food food){
    return inventoryRepository.findByFood(food);
  }

  public Inventory getInventoryByFoodAndWithoutVariant(Long foodId){
    return inventoryRepository.findByFoodIdAndProductVariantIdIsNull(foodId);
  }

  public Inventory getInventoryByFoodIdAndProductVariantId(Long foodId, Integer prodVarId){
    return inventoryRepository.findByFoodIdAndProductVariantId(foodId, prodVarId)
        .orElseThrow(() -> new ResourceNotFoundException("INVENTORY_NOT_EXIST", "the inventory does not exist"));
  }

  /**
   * Updates/Inserts the record to {@code Inventory} entity. Should only be used to {@code Food} entity with variants
   * @param patchRequest an object of {@code FoodPatchRequest}
   */
  public void patchInventoryByFoodIdWithVariant(ProductVariant productVariant, Food food, JsonNullable<Integer> quantity){
    Inventory inventory = getInventoryByFoodAndProductVariant(food, productVariant);
    if(inventory == null){
      inventory = new Inventory();

      if(food.getWithVariant()){
        Inventory oldInventory = getInventoryByFoodAndWithoutVariant(food.getId());

        if(oldInventory != null)
          inventoryRepository.delete(oldInventory);
      }
    }
    inventory.setFood(food);
    inventory.setProductVariant(productVariant);
    if(quantity != null)
      inventory.setQuantity(quantity.orElse(0));

    inventoryRepository.save(inventory);
  }

  /**
   * Updates/Inserts the record to {@code Inventory} entity. Should only be used to {@code Food} entity with variants
   * @param food an object of {@code Food}
   * @param quantity a value of {@code Integer}
   */
  public void patchInventoryByFoodIdWithoutVariant(Food food, Integer quantity){
    Inventory inventory = getInventoryByFood(food);
    if(inventory == null)
      inventory = new Inventory();
    inventory.setFood(food);
    inventory.setQuantity(quantity);

    inventoryRepository.save(inventory);
  }

  /**
   * Deletes a record
   * @param inventory an objcet of {@code Inventory}
   */
  public void deleteInventory(Inventory inventory){
    inventoryRepository.delete(inventory);
  }
  
  /**
   * Deletes a record that matches the food id and product variant id
   * @param foodId the id of {@code Food} entity to be deleted
   * @param prodVarId the id of {@code ProductVariant} entity to be deleted
   * @return None
   */
  public void deleteByFoodIdAndProductVariantId(Long foodId, Integer prodVarId){
    Inventory inventory = getInventoryByFoodIdAndProductVariantId(foodId, prodVarId);
    deleteInventory(inventory);
    
    LoggerFormat loggerFormat = LoggerFormat.builder()
        .level("INFO")
        .action("deleted inventory")
        .resourceId(inventory.getId().toString())
        .actorId(MDC.get(JWTCheckerPerRequest.USER_ID))
        .endpoint(MDC.get(JWTCheckerPerRequest.ENDPOINT))
        .build();

    log.atInfo()
        .setMessage("Inventory deleted successfully")
        .addKeyValue("action_info", loggerFormat)
        .log();
  }

  /**
   * Deletes a record on {@code Inventory} entity that matches the food id.
   * @param foodId the id of {@code Food} entity to be deleted
   * @return None
   */
  public void deleteByFoodId(Long foodId){
    deleteInventory(getInventoryByFoodAndWithoutVariant(foodId));
  }
}
