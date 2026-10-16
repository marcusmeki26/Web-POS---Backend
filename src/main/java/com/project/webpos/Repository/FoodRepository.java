package com.project.webpos.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.Food;
import com.project.webpos.Record.FoodImageOnly;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
  // List<Food> findByCategory_codeAndIsActiveAndIsDeletedNot(String categoryType);
  Food findByCategory(Category category);
  Food findFirstByCategory(Category category);
  Optional<FoodImageOnly> findProjectedById(Long foodId);
  Optional<Food> findByIdAndIsDeletedIsFalseAndIsActiveIsTrue(Long foodId);

  // =======================================================
  // CUSTOM QUERIES
  // =======================================================
  @Query("SELECT food from Food food WHERE food.isActive = true AND food.isDeleted = false")
  List<Food> findActiveAndNotDeleted();
  @Query("SELECT food from Food food " +
        "LEFT JOIN Category category " +
        "ON food.category = category " + 
        "WHERE category.id = :id AND food.isActive = true AND food.isDeleted = false")
  List<Food> findCategoryAndActiveAndNotDeleted(Integer id);
}
