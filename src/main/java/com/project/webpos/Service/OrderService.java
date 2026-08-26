package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.OrderType;
import com.project.webpos.Entity.TableList;
import com.project.webpos.Entity.TransactionSummary;
import com.project.webpos.Entity.DTO.OrderCreateRequest;
import com.project.webpos.Entity.DTO.OrderDeleteRequest;
import com.project.webpos.Entity.DTO.OrderPatchRequest;
import com.project.webpos.Entity.DTO.OrderResponse;
import com.project.webpos.Mapper.OrderMapper;
import com.project.webpos.Repository.OrderRepository;
import com.project.webpos.Repository.TransactionSummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;
  private final TableListService tableService;
  private final TransactionSummaryRepository transactionSummaryRepository;
  private final OrderTypeService orderTypeService;

  public List<OrderResponse> getOrders(){
    return orderMapper.toResponseList(orderRepository.findAll());
  }

  public List<OrderResponse> getOrderByFoodId(Long foodId){
    return orderMapper.toResponseList(orderRepository.findByFoodIdAndUnplaced(foodId));
  }

  public List<OrderResponse> getOrderByTableId(Integer tableId){
    return orderMapper.toResponseList(orderRepository.findByTable_Id(tableId));
  }

  public BigDecimal getOrderSumUnplaced(){
    return orderRepository.findAll()
            .stream()
            .filter(o -> o.getPrice() != null && o.getQuantity() != null && o.getPlaced() == false)
            .map(o -> o.getPrice().multiply(BigDecimal.valueOf(o.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public String getCurrentReceiptNo(){
    String unplacedReceiptNo = checkForUnplaced();

    if(!unplacedReceiptNo.isEmpty()){
      return unplacedReceiptNo;
    }

    OrderList orderList = orderRepository.findCurrentReceiptNo();

    if(orderList == null){
      // Check Transaction Summary
      TransactionSummary transactionSummary = transactionSummaryRepository.findCurrentReceiptNo();

      if(transactionSummary == null){
        return "R000001";
      }else{
        String removeChar = transactionSummary.getReceiptNo().replace("R", "");
        Integer incReceipt = Integer.parseInt(removeChar);
        incReceipt++;
        // %0 means pad 0 based on the length
        return String.format("R%0" + transactionSummary.getReceiptNo().length() + "d", incReceipt);
      }
    }

    if(orderList.getPlaced() == true){
      String removeChar = orderList.getReceiptNo().replace("R", "");
      Integer incReceipt = Integer.parseInt(removeChar);
      incReceipt++;
      return String.format("R%0" + (orderList.getReceiptNo().length() - incReceipt.toString().length()) + "d", incReceipt);
    }

    return orderList.getReceiptNo();
  }

  private String checkForUnplaced(){
    OrderList orderList = orderRepository.findUnplacedOrder();

    if(orderList != null){
      return orderList.getReceiptNo();
    }

    return "";
  }

  public List<OrderResponse> getUnplacedOrders(){
    return orderMapper.toResponseList(orderRepository.findUnplacedOrders());
  }

  // Updates only the quantity of a specific record
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

  // Updating of order into placed
  public Boolean patchOrder(String receiptNo, OrderPatchRequest patchRequest){
    List<OrderList> orderList = orderRepository.findByReceiptNo(receiptNo);
    
    if(orderList != null){
      if(patchRequest.getTableNo() == null || patchRequest.getPlaced() == null)
        throw new RuntimeException("Unable to update with the current value");

      orderList.forEach(order -> {
        if(patchRequest.getTableNo() != null){
          TableList tableList = tableService.getTableById(patchRequest.getTableNo());
          order.setTable(tableList);
        }
        
        order.setPlaced(patchRequest.getPlaced());

        if(patchRequest.getOrderType() != null){
          OrderType orderType = orderTypeService.getOrderTypeById(patchRequest.getOrderType());
          order.setOrderType(orderType);
        }

        orderRepository.save(order);
      });

      return true;
    }

    return false;
  }

  // Updating of order when being retrieved
  public Boolean patchRetrieveOrder(String receiptNo, OrderPatchRequest patchRequest){
    List<OrderList> orderList = orderRepository.findByReceiptNo(receiptNo);
    
    if(orderList != null){
      orderList.forEach(order -> {
        if(patchRequest.getTableNo() != null){
          TableList tableList = tableService.getTableById(patchRequest.getTableNo());
          order.setTable(tableList);
        }else{
          order.setTable(null);
        }
        
        order.setPlaced(patchRequest.getPlaced());

        if(patchRequest.getOrderType() != null){
          OrderType orderType = orderTypeService.getOrderTypeById(patchRequest.getOrderType());
          order.setOrderType(orderType);
        }else{
          order.setOrderType(null);
        }

        orderRepository.save(order);
      });

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

  public Boolean deleteOrder(OrderDeleteRequest request){
    if(request.getReceiptNo() != null || request.getFoodId() != null){
      OrderList orderList = orderRepository.findByReceiptNoAndFoodId(request.getReceiptNo(), request.getFoodId());

      orderRepository.delete(orderList);

      return true;
    }

    return false;
  }
}
