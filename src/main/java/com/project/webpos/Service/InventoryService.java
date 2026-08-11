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
}
