package com.project.webpos.Service.Shared;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Size;
import com.project.webpos.Repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ValidationManagementService {
  private final ProductVariantRepository productVariantRepository;

  /**
   * Checks whether the size has a reference on {@code ProductVariant} entity
   * @param size an object of {@code Size}
   * @return true if no record found.
   */
  public boolean isFoodHasExistingSize(Size size){
    return productVariantRepository.findFirstBySize_id(size.getId()) != null;
  }
}
