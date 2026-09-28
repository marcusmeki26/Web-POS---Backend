package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.ProductVariant;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{
  Inventory findByFoodAndProductVariant(Food food, ProductVariant productVariant);
  Inventory findByFood(Food food);
  Inventory findByFoodIdAndProductVariantIdIsNull(Long foodId);
  Inventory findByFoodIdAndProductVariantId(Long foodId, Integer prodVarId);
}
