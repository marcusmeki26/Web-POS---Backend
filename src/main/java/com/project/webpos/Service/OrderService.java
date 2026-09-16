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
import com.project.webpos.Enum.EntryType;
import com.project.webpos.Entity.DTO.OrderPaymentCreateRequest;
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
    return orderMapper.toResponseList(orderRepository.findByNumberAndUnplaced(foodId));
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

  public List<OrderResponse> getUnplacedOrders(){
    return orderMapper.toResponseList(orderRepository.findUnplacedOrders());
  }

  /**
   * Updates the quantity and amount of the specific order
   * @param id The id of the order
   * @param patchRequest The object of OrderPatchRequest class
   * @return true if updating is successful
   */
  public Boolean patchOrder(Integer id, OrderPatchRequest patchRequest){
    OrderList orderList = orderRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Invalid order value"));
    
    if(orderList != null){
      if(patchRequest.getQuantity() != null){
        orderList.setQuantity(patchRequest.getQuantity());
        BigDecimal price = orderList.getPrice();
        BigDecimal amount = BigDecimal.valueOf(0);
        BigDecimal newDiscAmt = BigDecimal.valueOf(0);
        Integer qty = orderList.getQuantity();
        Float discPercent = orderList.getDiscountPercent();      

        if(orderList.getDiscountPercent() != Float.valueOf(0)){
          newDiscAmt = price.multiply(BigDecimal.valueOf(qty)).multiply(BigDecimal.valueOf(discPercent / 100));
          orderList.setDiscountAmount(newDiscAmt);
        }

        amount = price.multiply(BigDecimal.valueOf(qty)).subtract(newDiscAmt);
        orderList.setAmount(amount);
      }

      if(patchRequest.getReceiptNo() != null){
        orderList.setReceiptNo(patchRequest.getReceiptNo());
      }

      orderRepository.save(orderList);

      return true;
    }

    return false;
  }

  public OrderList getOrderByFoodIdAndTableId(Long foodId, Integer tableId){
    return orderRepository.findByNumberAndTableId(foodId, tableId);
  }

  public OrderList getById(Integer id){
    return orderRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("No existing order id"));
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

  public Boolean patchAddedOrder(String receiptNo, List<OrderPatchRequest> orderPatchRequests){
    orderPatchRequests.forEach(order -> {
      OrderList orderListSave, orderListDel;
      if(order.getSizeId() == null)
        orderListSave = getOrderByFoodIdAndTableId(order.getNumber(), order.getTableNo());
      else // Query with sizes
        orderListSave = orderRepository.findByNumberAndTableIdAndSizeId(order.getNumber(), order.getTableNo(), order.getSizeId());

      if(orderListSave != null){ // Has existing record on the table
        orderListSave.setQuantity((orderListSave.getQuantity() + order.getQuantity()));

        orderRepository.save(orderListSave);
        orderListDel = getById(order.getId());
        orderRepository.delete(orderListDel);
      }else{ // No existing record on the table
        OrderList orderList = getById(order.getId());
        
        orderList.setReceiptNo(receiptNo);
        orderList.setPlaced(true);

        TableList tableList = tableService.getTableById(order.getTableNo());
        orderList.setTable(tableList);
        
        orderRepository.save(orderList);
      }
    });

    return true;
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
    BigDecimal amount;

    order.setReceiptNo(request.getReceiptNo());
    order.setNumber(request.getNumber());
    order.setShortFoodDesc(request.getShortFoodDesc());
    order.setPrice(request.getPrice());
    order.setDiscountPercent(request.getDiscountPercent());
    order.setEntryType(request.getEntryType());
    order.setSizeId(request.getSizeId());
    order.setSize(request.getSize());
    order.setQuantity(request.getQuantity());

    BigDecimal price = order.getPrice();
    BigDecimal discountAmount = order.getDiscountAmount();
    amount = price.subtract(discountAmount).multiply(BigDecimal.valueOf(order.getQuantity()));
    order.setAmount(amount);

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

  /**
   * Processing of payment either partial or full payment.
   * @param request The object of OrderPaymentCreateRequest class
   * @return true if the payment is inserted successfully.
   */
  public Boolean setPayment(OrderPaymentCreateRequest request){
    BigDecimal totalAmt = request.getTotalAmount();
    BigDecimal payment = request.getPayment();
    
    OrderList order = new OrderList();
    order.setPrice(BigDecimal.valueOf(0.00));
    order.setQuantity(1);
    order.setPlaced(true);
    order.setShortFoodDesc(request.getShortFoodDesc());
    order.setAmount(payment);
    order.setEntryType(EntryType.PAYMENT);
    order.setReceiptNo(request.getReceiptNo());
    order.setNumber(request.getNumber());
    
    if(request.getTableNo() != null){
      TableList tableList = tableService.getTableById(request.getTableNo());
      order.setTable(tableList);
    }

    orderRepository.save(order);
    
    if(payment.compareTo(totalAmt) == -1)
      return false;
    
    return true;
  }

  /**
   * Deletes the record based on receipt no. & number field. 
   * @param request The object of OrderDeleteRequest class
   * @return true if the deletion is successful
   */
  public Boolean deleteOrder(OrderDeleteRequest request){
    if(request.getReceiptNo() != null || request.getNumber() != null){
      OrderList orderList = orderRepository.findByReceiptNoAndNumber(request.getReceiptNo(), request.getNumber());

      orderRepository.delete(orderList);

      return true;
    }

    return false;
  }

  // Utilities
  private String checkForUnplaced(){
    OrderList orderList = orderRepository.findUnplacedOrder();

    if(orderList != null){
      return orderList.getReceiptNo();
    }

    return "";
  }
}
