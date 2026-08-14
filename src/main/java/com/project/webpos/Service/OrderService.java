package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.TableList;
import com.project.webpos.Entity.DTO.OrderCreateRequest;
import com.project.webpos.Entity.DTO.OrderPatchRequest;
import com.project.webpos.Entity.DTO.OrderResponse;
import com.project.webpos.Mapper.OrderMapper;
import com.project.webpos.Repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;
  private final TableService tableService;

  public List<OrderResponse> getOrders(){
    return orderMapper.toResponseList(orderRepository.findAll());
  }

  public OrderResponse getOrder(Long foodId){
    return orderMapper.toResponse(orderRepository.findByFoodId(foodId));
    // return new OrderResponse();
  }

  public Boolean patchOrder(Integer id, OrderPatchRequest patchRequest){
    OrderList orderList = orderRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Invalid order value"));
    
    if(orderList != null){
      if(patchRequest.getQuantity() != null){
        orderList.setQuantity(patchRequest.getQuantity());
      }

      orderRepository.save(orderList);

      return true;
    }

    return false;
  }

  public Boolean setOrder(OrderCreateRequest request){
    OrderList order = new OrderList();

    order.setReceiptNo(request.getReceiptNo());
    order.setFoodId(request.getFoodId());
    order.setShortFoodDesc(request.getShortFoodDesc());
    order.setPrice(request.getPrice());
    order.setSizeId(request.getSizeId());
    order.setSize(request.getSize());
    order.setQuantity(request.getQuantity());

    // Add getting of table object
    if(request.getTableId() != null){
      TableList table = tableService.getTableById(request.getTableId());
      order.setTable(table);
    }

    if(orderRepository.save(order) != null){
      return true;
    }

    return false;
  }
}
