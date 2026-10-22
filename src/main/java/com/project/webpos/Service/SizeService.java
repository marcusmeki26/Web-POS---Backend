package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Size;
import com.project.webpos.Repository.SizeRepository;
import com.project.webpos.Service.Shared.ValidationManagementService;
import com.project.webpos.Entity.DTO.SizeResponse;
import com.project.webpos.Exception.ConflictException;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.Entity.DTO.SizeCreateRequest;
import com.project.webpos.Entity.DTO.SizeCreateResponse;
import com.project.webpos.Entity.DTO.SizePatchRequest;
import com.project.webpos.Entity.DTO.SizePatchResponse;
import com.project.webpos.Mapper.SizeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SizeService {
  private final SizeRepository sizeRepository;
  private final SizeMapper sizeMapper;
  private final ValidationManagementService validationManagementService;

  /**
   * Gets the size based on the target ID. Throws a ResourceNotFoundException if it is not existing
   * @param id the {@code Integer} value of the target ID
   * @return the object of {@code Size}
   */
  public Size getSize(Integer id){
    return sizeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SIZE_NOT_EXIST", "size does not exist"));
  }

  /**
   * Retrieves all the sizes
   * @return an object of {@code List<SizeResponse>}
   */
  public List<SizeResponse> getSizes(){
    return sizeMapper.toResponseList(sizeRepository.findAll());
  }

  /**
   * Creates a new record on {@code Size} entity
   * @param postRequest an object 
   * @return an object of {@code SizeCreateResponse}
   */
  public SizeCreateResponse postSize(SizeCreateRequest postRequest){
    Size size = Size.builder()
        .sizeName(postRequest.getSizeName())
        .build();

    return sizeMapper.toCreateResponse(sizeRepository.save(size));
  }

  /**
   * Partially modifies the target size based on the given payload
   * @param patchRequest an object of {@code SizePatchRequest}
   * @param id the ID of the target {@code Size} to modify
   * @return an object of {@code SizePatchResponse}
   */
  public SizePatchResponse patchSize(SizePatchRequest patchRequest, Integer id){
    if(patchRequestHasRecord(patchRequest)){
      Size size = getSize(id);
      sizeMapper.patchSize(patchRequest, size);

      return sizeMapper.toPatchResponse(sizeRepository.save(size));
    }

    return new SizePatchResponse();
  }

  /**
   * Hard deletes a record in {@code Size} entity based on the target ID
   * @param id an integer value of the target id
   */
  public void deleteSize(Integer id){
    Size size = getSize(id);
    if(validationManagementService.isFoodHasExistingSize(size))
      throw new ConflictException("UNABLE_TO_DELETE", "deletion of size", "unable to delete since the size is still used by a food");
    
    sizeRepository.delete(size);
  }

  // =======================================================
  // UTILITIES
  // =======================================================
  /**
   * Checks whether the {@code SizePatchRequest} has present values
   * @param patchRequest an object of {@code SizePatchRequest}
   * @return true if there is a present value
   */
  private boolean patchRequestHasRecord(SizePatchRequest patchRequest){
    if(patchRequest == null)
      return false;

    return (patchRequest.getSizeName().isPresent());
  }

  // public List<SizeResponse> getSizeByFoodId(Long foodId){
  //   return List.of(new SizeResponse());
  // }
}
