package com.project.webpos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
  List<Food> findByCategory_code(String categoryType);
}
