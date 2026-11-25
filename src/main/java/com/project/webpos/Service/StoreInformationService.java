package com.project.webpos.Service;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.StoreInformation;
import com.project.webpos.Entity.DTO.StoreInformationPutRequest;
import com.project.webpos.Entity.DTO.StoreInformationResponse;
import com.project.webpos.Mapper.StoreInformationMapper;
import com.project.webpos.Record.CompanyInformationResponse;
import com.project.webpos.Record.TaxPercentResponse;
import com.project.webpos.Repository.StoreInformationRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class StoreInformationService {
  private final StoreInformationRepository storeInformationRepository;
  private final StoreInformationMapper storeInformationMapper;

  /**
   * 
   * @return
   */
  public StoreInformationResponse getStoreInformation(){
    return storeInformationMapper.toResponse(
      storeInformationRepository.findFirstBy()
          .orElse(new StoreInformation())
    );
  }

  /**
   * 
   * @return
   */
  public CompanyInformationResponse getCompanyInformation(){
    return storeInformationRepository.findCompanyInformationBy();
  }

  /**
   * 
   * @return
   */
  public TaxPercentResponse getTaxPercent(){
    return storeInformationRepository.findTaxPercentBy();
  }

  /**
   * 
   * @param storeInformationUpdateRequest
   * @return
   */
  public StoreInformationResponse updateStoreInformation(StoreInformationPutRequest storeInformationUpdateRequest){
    StoreInformation storeInformation = storeInformationRepository.findFirstBy()
        .orElse(new StoreInformation());

    storeInformationMapper.toEntity(storeInformationUpdateRequest, storeInformation);
    return storeInformationMapper.toResponse(storeInformationRepository.save(storeInformation));
  }
}
