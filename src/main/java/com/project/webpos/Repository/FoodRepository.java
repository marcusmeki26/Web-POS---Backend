package com.project.webpos.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Food;
import com.project.webpos.Record.FoodImageOnly;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
  List<Food> findByCategory_code(String categoryType);
  Optional<FoodImageOnly> findProjectedById(Long foodId);
}
