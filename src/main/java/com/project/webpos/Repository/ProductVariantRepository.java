package com.project.webpos.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer>{
  List<ProductVariant> findByFood_id(Long id);
  Optional<ProductVariant> findByFoodIdAndSizeId(Long food, Integer size);
}
