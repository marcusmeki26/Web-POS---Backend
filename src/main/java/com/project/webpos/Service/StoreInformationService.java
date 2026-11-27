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
   * Retrieves the record from store information. If no record is found, returns the default value of each fields
   * @return an object of {@code StoreInformationResponse} that contains the record
   */
  public StoreInformationResponse getStoreInformation(){
    return storeInformationRepository.findFirstBy()
        .map(record -> storeInformationMapper.toResponse(record))
        .orElseGet(() -> storeInformationMapper.toResponse(new StoreInformation()));
  }

  /**
   * Retrieves only the company information from store information. If no record is found, returns the default value of each fields
   * @return an object of {@code CompanyInformationResponse} that conatins the company information record
   */
  public CompanyInformationResponse getCompanyInformation(){
    return storeInformationRepository.findCompanyInformationBy()
        .orElseGet(() -> storeInformationMapper.toCompanyInformationResponse(new StoreInformation()));
  }

  /**
   * Retrieves only the tax percent. If no record is found, <b>12%</b> will be the default value
   * @return an object of {@code TaxPercentResponse} that contains the tax percent
   */
  public TaxPercentResponse getTaxPercent(){
    return storeInformationRepository.findTaxPercentBy()
        .orElse(new TaxPercentResponse(12));
  }

  /**
   * Performs an update on store information record based on user's request
   * @param storeInformationUpdateRequest contains update request payload
   * @return an object of {@code StoreInformationResponse} that contains the updated entity
   */
  public StoreInformationResponse updateStoreInformation(StoreInformationPutRequest storeInformationUpdateRequest){
    StoreInformation storeInformation = storeInformationRepository.findFirstBy()
        .orElseGet(() -> new StoreInformation());

    storeInformationMapper.toEntity(storeInformationUpdateRequest, storeInformation);
    return storeInformationMapper.toResponse(storeInformationRepository.save(storeInformation));
  }
}
