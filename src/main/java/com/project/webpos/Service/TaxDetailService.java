package com.project.webpos.Service;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.DTO.TaxDetailResponse;
import com.project.webpos.Mapper.TaxDetailMapper;
import com.project.webpos.Repository.TaxDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxDetailService {
  private final TaxDetailRepository taxDetailRepository;
  private final TaxDetailMapper taxDetailMapper;

  public TaxDetailResponse getActiveTax(Boolean isActive){
    return taxDetailMapper.toResponse(taxDetailRepository.findByIsActive(isActive));
  }
}
