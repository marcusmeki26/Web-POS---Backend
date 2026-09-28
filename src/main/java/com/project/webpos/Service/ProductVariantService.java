package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.Size;
import com.project.webpos.Entity.DTO.ProductVariantPatchRequest;
import com.project.webpos.Repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
  private final ProductVariantRepository productVariantRepository;
  private final SizeService sizeService;
  private final InventoryService inventoryService;

  // Used for initializing menu (not permanent)
  public void postProdVarInit(Food food, Size size, Double price){
    ProductVariant prodVar = new ProductVariant();
    prodVar.setFood(food);
    prodVar.setSize(size);
    prodVar.setPrice(BigDecimal.valueOf(price));

    productVariantRepository.save(prodVar);
  }

  /**
   * Returns a {@code List} of objects of {@code ProductVariant} entity based on the specified {@code Food} id
   * @param id the id of {@code Food} entity
   * @return a {@code List<ProductVariant>}
   */
  public List<ProductVariant> getProdVarByFoodId(Long id){
    return productVariantRepository.findByFood_id(id);
  }
  
  /**
   * Returns an object of {@code ProductVariant} entity
   * @param id the id of {@code ProductVariant} to be searched
   * @return an object of {@code ProductVariant}
   */
  public ProductVariant getProductVariantById(Integer id){
    return productVariantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No exisitng variant found."));
  }

  /**
   * Returns an object of {@code ProductVariant} entity based on {@code Food} and {@code Size}
   * @param food an object of {@code Food} entity
   * @param size an object of {@code Size} entity
   * @returns an object of {@code ProductVariant}
   */
  public ProductVariant getProductVariantByFoodAndSize(Long foodId, Integer sizeId){
    return productVariantRepository.findByFoodIdAndSizeId(foodId, sizeId);
  }

  /**
   * Updates/Inserts the record to {@Code ProductVariant} entity
   * @param patchRequests an object of {@code List<ProductVariantPatchRequest} 
   * @param food an object of {@code Food}
   */
  public void patchProdVarByFoodId(Food food, List<ProductVariantPatchRequest> patchRequests){
    patchRequests.forEach(variant -> {
      Size size = sizeService.getSize(variant.getSizeId());
      ProductVariant productVariant = getProductVariantByFoodAndSize(food.getId(), variant.getSizeOldId());

      if(productVariant == null)
        productVariant = new ProductVariant();
      productVariant.setFood(food);
      productVariant.setPrice(variant.getSizePrice());
      productVariant.setSize(size);

      productVariantRepository.save(productVariant);

      inventoryService.patchInventoryByFoodIdWithVariant(productVariant, food, variant.getSizeQuantity());
    });
  }

  /**
   * Deletes a record
   * @param productVariant an object of {@code ProductVariant}
   */
  public void deteleProductVariant(ProductVariant productVariant){
    productVariantRepository.delete(productVariant);
  }
}
