package com.project.webpos.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.DTO.CategoryCountResponse;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
  Optional<Category> findByIdAndIsActiveIsTrueAndIsDeletedIsFalse(Integer id);
  List<Category> findAllByIsActiveIsTrueAndIsDeletedIsFalse();

  // =======================================================
  // CUSTOM QUERIES
  // =======================================================
  @Query("SELECT new com.project.webpos.Entity.DTO.CategoryCountResponse(c.code, c.id, COUNT(f)) " +
       "FROM Category c " +
       "LEFT JOIN Food f " +
       "ON f.category = c AND f.isActive = true AND f.isDeleted = false " +
       "WHERE c.isActive = true AND c.isDeleted = false " + 
       "GROUP BY c.code, c.id")
  List<CategoryCountResponse> getCategoriesCount();
}
