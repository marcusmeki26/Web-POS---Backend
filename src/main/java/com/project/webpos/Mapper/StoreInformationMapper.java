package com.project.webpos.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.webpos.Entity.StoreInformation;
import com.project.webpos.Entity.DTO.StoreInformationPutRequest;
import com.project.webpos.Entity.DTO.StoreInformationResponse;

@Mapper(componentModel = "spring")
public interface StoreInformationMapper {
  StoreInformationResponse toResponse(StoreInformation storeInformation);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  StoreInformation postToEntity(StoreInformationPutRequest storeInformationPostOrPutRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void toEntity(StoreInformationPutRequest storeInformationPostOrPutRequest, @MappingTarget StoreInformation storeInformation);
}
