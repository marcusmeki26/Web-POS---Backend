package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.Size;
import com.project.webpos.Repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
  private final ProductVariantRepository productVariantRepository;

  // Used for initializing menu (not permanent)
  public void postProdVarInit(Food food, Size size, Double price){
    ProductVariant prodVar = new ProductVariant();
    prodVar.setFood(food);
    prodVar.setSize(size);
    prodVar.setPrice(BigDecimal.valueOf(price));

    productVariantRepository.save(prodVar);
  }

  public List<ProductVariant> getProdVarByFoodId(Long id){
    return productVariantRepository.findByFood_id(id);
  }
}
