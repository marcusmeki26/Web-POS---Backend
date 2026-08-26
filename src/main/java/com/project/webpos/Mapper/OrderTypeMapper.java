package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.project.webpos.Entity.OrderType;
import com.project.webpos.Entity.DTO.OrderTypeResponse;

@Mapper(componentModel = "spring")
public interface OrderTypeMapper {
  OrderTypeResponse toResponse(OrderType orderType);

  List<OrderTypeResponse> toResponseList(List<OrderType> orderTypes);
}
