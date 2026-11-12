package com.project.webpos.Mapper;


import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.project.webpos.Entity.TransactionSummary;
import com.project.webpos.Entity.DTO.TransactionSummaryResponse;

@Mapper(componentModel = "spring")
public interface TransactionSummaryMapper {
  TransactionSummaryResponse toResponse(TransactionSummary transactionSummary);
  default Page<TransactionSummaryResponse> toResponseContent(Page<TransactionSummary> transactionSummaries){
      if(transactionSummaries == null)
        return null;
  
      return transactionSummaries.map(this::toResponse);
  }
}
