package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.DTO.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  @Mapping(source = "table.id", target = "tableId")
  OrderResponse toResponse(OrderList order);

  List<OrderResponse> toResponseList(List<OrderList> orderList);
}
