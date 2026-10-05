package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Food;
import com.project.webpos.Entity.ProductVariant;
import com.project.webpos.Entity.Size;
import com.project.webpos.Entity.DTO.ProductVariantCreateRequest;
import com.project.webpos.Entity.DTO.ProductVariantPatchRequest;
import com.project.webpos.Repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
  private final ProductVariantRepository productVariantRepository;
  private final SizeService sizeService;
  private final InventoryService inventoryService;

  /**
   * Creates a new record of {@code ProductVariant}
   * @param food an object of {@code Food}
   * @param price the price of the {@code ProductVariant}
   * @return an object of {@code ProductVariant}
   */
  public ProductVariant setProductVariant(Food food, ProductVariantCreateRequest variant){
    ProductVariant productVariant = new ProductVariant();
    productVariant.setPrice(variant.getSizePrice());
    productVariant.setFood(food);

    Size size = sizeService.getSize(variant.getSizeId());
    productVariant.setSize(size);
    
    return productVariantRepository.save(productVariant);
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
  public void deleteProductVariant(ProductVariant productVariant){
    productVariantRepository.delete(productVariant);
  }

  // Can delete if no service will use this :REMOVE-COMMENT
  // public ProductVariant deleteByFoodAndSize(Long foodId, Integer sizeId){
  //   ProductVariant productVariant = getProductVariantByFoodAndSize(foodId, sizeId);
  //   if(productVariant == null)
  //     throw new RuntimeException("No existing product variant");

  //   deleteProductVariant(productVariant);

  //   return productVariant;
  // }

  /**
   * Deletes the record on {@code ProductVariant} entity by its food id & {@code Inventory} entity by its food id & product variant id
   * @param foodId the id of {@code Food} entity to be deleted
   */
  public void deleteByFoodIdAndInventory(Long foodId){
    List<ProductVariant> variants = getProdVarByFoodId(foodId);
    if(variants.size() > 0 || variants != null){
      variants.forEach(variant -> {
        inventoryService.deleteByFoodIdAndProductVariantId(foodId, variant.getId());
        deleteProductVariant(variant);
      });
    }
  }
}
