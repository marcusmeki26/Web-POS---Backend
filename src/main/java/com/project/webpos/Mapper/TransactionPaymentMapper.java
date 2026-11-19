package com.project.webpos.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.project.webpos.Entity.TransactionPayment;
import com.project.webpos.Entity.DTO.TransactionPaymentResponse;

@Mapper(componentModel = "spring")
public interface TransactionPaymentMapper {
  @Mapping(target = "tableId", source = "table.id")
  TransactionPaymentResponse toResponse(TransactionPayment transactionPayment);
  default Page<TransactionPaymentResponse> toResponseContent(Page<TransactionPayment> transactionPaymentPage){
    if(transactionPaymentPage == null) 
      return null;

    return transactionPaymentPage.map(this::toResponse);
  }
}
