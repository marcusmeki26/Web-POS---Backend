package com.project.webpos.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.Inventory;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.DTO.InventorySummaryRequest;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{
  Inventory findByFoodAndProductVariant(Food food, ProductVariant productVariant);
  Inventory findByFood(Food food);
  Inventory findByFoodIdAndProductVariantIdIsNull(Long foodId);
  Optional<Inventory> findByFoodIdAndProductVariantId(Long foodId, Integer prodVarId);

  @Query("""
    SELECT new com.project.webpos.Entity.DTO.InventorySummaryRequest(f.id, f.foodName, pv.id, s.sizeName, i.quantity, i.createdAt, i.updatedAt) 
    FROM Inventory i 
    JOIN i.food f 
    LEFT JOIN i.productVariant pv 
    LEFT JOIN pv.size s 
    WHERE f.isDeleted = false AND f.isActive = true
  """)
  Page<List<InventorySummaryRequest>> findAllInventorySummary(Pageable pageable);
}
