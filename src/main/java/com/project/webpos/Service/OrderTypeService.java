package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderType;
import com.project.webpos.Entity.DTO.OrderTypeResponse;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.Mapper.OrderTypeMapper;
import com.project.webpos.Repository.OrderTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderTypeService {
  private final OrderTypeRepository orderTypeRepository;
  private final OrderTypeMapper orderTypeMapper;

  public List<OrderTypeResponse> getOrderTypes(){
    return orderTypeMapper.toResponseList(orderTypeRepository.findAll());
  }

  public OrderType getOrderTypeById(Integer orderId){
    return orderTypeRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("ORDER_TYPE_NOT_EXIST", "No existing record"));
  }
}
