package com.project.webpos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.webpos.Entity.Category;
import com.project.webpos.Entity.DTO.CategoryCountResponse;

public interface CategoryRepository extends JpaRepository<Category, String> {

  @Query("SELECT new com.project.webpos.Entity.DTO.CategoryCountResponse(c.code, COUNT(f)) " +
       "FROM Category c " +
       "LEFT JOIN Food f ON f.category = c " +
       "GROUP BY c.code")
  List<CategoryCountResponse> getCategoriesCount();
}
