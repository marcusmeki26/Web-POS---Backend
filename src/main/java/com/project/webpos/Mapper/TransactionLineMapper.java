package com.project.webpos.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.project.webpos.Entity.TransactionLine;
import com.project.webpos.Entity.DTO.TransactionLineResponse;

@Mapper(componentModel = "spring")
public interface TransactionLineMapper {
  @Mapping(target = "tableId", source = "table.id")
  @Mapping(target = "foodDescription", source = "shortFoodDesc")
  TransactionLineResponse toResponse(TransactionLine transactionLine);

  default Page<TransactionLineResponse> toResponsePage(Page<TransactionLine> transactionLinePage){
    if(transactionLinePage == null)
      return null;

    return transactionLinePage.map(this::toResponse);
  }
}
