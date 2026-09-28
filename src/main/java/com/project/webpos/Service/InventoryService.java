package com.project.webpos.Service;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  // Used for initializing menu (not permanent)
  public void setInventoryInit(Food food, Integer qty){
    Inventory inventory = new Inventory();

    inventory.setFood(food);
    inventory.setQuantity(qty);

    inventoryRepository.save(inventory);
  }

  public void setInventoryInit(Food food, ProductVariant productVariant, Integer qty){
    Inventory inventory = new Inventory();

    inventory.setFood(food);
    inventory.setProductVariant(productVariant);
    inventory.setQuantity(qty);

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
    return inventoryRepository.findByFoodIdAndProductVariantId(foodId, prodVarId);
  }

  /**
   * Updates/Inserts the record to {@code Inventory} entity. Should only be used to {@code Food} entity with variants
   * @param patchRequest an object of {@code FoodPatchRequest}
   */
  public void patchInventoryByFoodIdWithVariant(ProductVariant productVariant, Food food, Integer quantity){
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
    inventory.setQuantity(quantity);

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
}
