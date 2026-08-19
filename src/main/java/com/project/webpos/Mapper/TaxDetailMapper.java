package com.project.webpos.Mapper;

import org.mapstruct.Mapper;

import com.project.webpos.Entity.TaxDetail;
import com.project.webpos.Entity.DTO.TaxDetailResponse;

@Mapper(componentModel = "spring")
public interface TaxDetailMapper {
  TaxDetailResponse toResponse(TaxDetail taxDetail);
}
