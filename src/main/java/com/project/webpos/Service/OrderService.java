package com.project.webpos.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.project.webpos.Entity.DTO.OrderPaymentResponse;
import com.project.webpos.Mapper.OrderMapper;
import com.project.webpos.Repository.OrderRepository;
import com.project.webpos.Repository.TransactionSummaryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
  // Remove all Boolean return values in Insertion Statement. Create exception handler for failed insertion :REMOVE-COMMENT
  // Change hard coded values like 1.12 & .12 into setup based :REMOVE-COMMENT
  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;
  private final TableListService tableService;
  private final TransactionSummaryRepository transactionSummaryRepository;
  private final OrderTypeService orderTypeService;
  private final TransactionLineService transactionLineService;
  private final TransactionPaymentService transactionPaymentService;
  private final TransactionSummaryService transactionSummaryService;

  public List<OrderResponse> getOrders(){
    return orderMapper.toResponseList(orderRepository.findAll());
  }

  public List<OrderResponse> getOrderByFoodId(Long foodId){
    return orderMapper.toResponseList(orderRepository.findByNumberAndUnplaced(foodId));
  }

  /**
   * Gets the {@code List<OrderList>} and returns it in a {@code List<OrderResponse>} format
   * @param tableId 
   * @return {@code List<OrderReponse>}
   */
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
        return String.format("R%0" + removeChar.length() + "d", incReceipt);
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
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal newDiscAmt = BigDecimal.ZERO;
        BigDecimal newVatAmt = BigDecimal.ZERO;
        Integer qty = orderList.getQuantity();
        Float discPercent = orderList.getDiscountPercent();      

        if(orderList.getDiscountPercent() != Float.valueOf(0)){
          newDiscAmt = price.multiply(BigDecimal.valueOf(qty)).multiply(BigDecimal.valueOf(discPercent / 100));
          orderList.setDiscountAmount(newDiscAmt);
        }

        amount = price.multiply(BigDecimal.valueOf(qty)).subtract(newDiscAmt);
        newVatAmt = amount.divide(BigDecimal.valueOf(1.12), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(.12));
        orderList.setAmount(amount);
        orderList.setVatAmount(newVatAmt);
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

  /**
   * Appending of new order to a table that has existing orders.
   * @param receiptNo value of {@code String} the receipt no. of the table that has existing orders 
   * @param orderPatchRequests value of {@code List<OrderPatchRequest>} the order that will be added to the table
   */
  public void patchAddedOrder(String receiptNo, List<OrderPatchRequest> orderPatchRequests){
    orderPatchRequests.forEach(order -> {
      OrderList orderListSave, orderListDel;
      if(order.getSizeId() == null)
        orderListSave = getOrderByFoodIdAndTableId(order.getNumber(), order.getTableNo());
      else // Query with sizes
        orderListSave = orderRepository.findByNumberAndTableIdAndSizeId(order.getNumber(), order.getTableNo(), order.getSizeId());

      if(orderListSave != null){ // Has existing record on the table
        orderListSave.setQuantity((orderListSave.getQuantity() + order.getQuantity()));
        BigDecimal newDiscAmt = BigDecimal.ZERO;
        BigDecimal tempNewAmt = orderListSave.getPrice().multiply(BigDecimal.valueOf(orderListSave.getQuantity())); 
        if(orderListSave.getDiscountPercent() != 0){
          Float discPercent = orderListSave.getDiscountPercent();
          newDiscAmt = tempNewAmt.multiply(BigDecimal.valueOf(discPercent).multiply(BigDecimal.valueOf((discPercent / 100))));
        }
        BigDecimal newAmount = tempNewAmt.subtract(newDiscAmt);
        orderListSave.setAmount(newAmount);
        orderListSave.setDiscountAmount(newDiscAmt);

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

  /**
   * Inserts a new order into Order List table
   * @param request value of {@code OrderCreateRequest}
   * @return {@code void}
   */
  public void setOrder(OrderCreateRequest request){
    OrderList order = new OrderList();

    order.setReceiptNo(request.getReceiptNo());
    order.setNumber(request.getNumber());
    order.setShortFoodDesc(request.getShortFoodDesc());
    order.setPrice(request.getPrice());
    order.setDiscountPercent(request.getDiscountPercent());
    order.setEntryType(request.getEntryType());
    order.setSizeId(request.getSizeId());
    order.setSize(request.getSize());
    order.setQuantity(request.getQuantity());
    
    BigDecimal totalPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
    // Turn discount percent into decimal
    BigDecimal discountPercent = BigDecimal.valueOf(order.getDiscountPercent()).multiply(BigDecimal.valueOf(100));
    BigDecimal discountAmount = totalPrice.multiply(discountPercent);

    BigDecimal amount = totalPrice.subtract(discountAmount);
    BigDecimal vatAmount = amount.divide(BigDecimal.valueOf(1.12),  2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(.12));
    order.setAmount(amount);
    order.setDiscountAmount(discountAmount);
    order.setVatAmount(vatAmount);
    
    // Add getting of table object
    if(request.getTableId() != null){
      TableList table = tableService.getTableById(request.getTableId());
      order.setTable(table);
    }

    orderRepository.save(order);
  }

  /**
   * Processing of payment either partial or full payment.
   * Will move the transaction into Transaction Line & Transaction Summary table if the payment is complete
   * @param request The object of OrderPaymentCreateRequest class
   * @return true if the payment is inserted successfully.
   */
  @Transactional 
  public OrderPaymentResponse setPayment(OrderPaymentCreateRequest request){
    if(request.getReceiptNo() == null || request.getTableNo() == null)
        throw new RuntimeException("Cannot process request without receipt no or table no");
    
    BigDecimal totalAmt = request.getTotalAmount();
    BigDecimal payment = request.getPayment();
    
    OrderList orderPaymentLine = new OrderList();
    orderPaymentLine.setPrice(BigDecimal.valueOf(0.00));
    orderPaymentLine.setQuantity(1);
    orderPaymentLine.setPlaced(true);
    orderPaymentLine.setShortFoodDesc(request.getShortFoodDesc());
    orderPaymentLine.setAmount(payment);
    orderPaymentLine.setEntryType(EntryType.PAYMENT);
    orderPaymentLine.setReceiptNo(request.getReceiptNo());
    orderPaymentLine.setNumber(request.getNumber());
    
    TableList tableList = tableService.getTableById(request.getTableNo());
    orderPaymentLine.setTable(tableList);

    orderRepository.save(orderPaymentLine);
    
    if(payment.compareTo(totalAmt) == -1){
      return new OrderPaymentResponse(false, null);
    }
    else{
      // Insert change line
      BigDecimal change = payment.subtract(totalAmt);
      if(change.compareTo(BigDecimal.valueOf(0)) == 1){
        OrderList orderChangeLine = new OrderList(); 
        orderChangeLine.setPrice(BigDecimal.valueOf(0.00));
        orderChangeLine.setQuantity(1);
        orderChangeLine.setPlaced(true);
        orderChangeLine.setShortFoodDesc("Cash change");
        orderChangeLine.setAmount(change.negate());
        orderChangeLine.setEntryType(EntryType.PAYMENT);
        orderChangeLine.setReceiptNo(request.getReceiptNo());
        orderChangeLine.setNumber(request.getNumber());
        
        orderChangeLine.setTable(tableList);

        orderRepository.save(orderChangeLine);
      }
      
      List<OrderList> orders = orderRepository.findByTable_Id(request.getTableNo());
      List<OrderList> ordersByOrder = orders.stream()
                                        .filter(o -> o.getEntryType() == EntryType.ORDER)
                                        .toList();
      if(ordersByOrder != null) 
        transactionLineService.setTransactionLine(ordersByOrder);
      
      List<OrderList> ordersByPayment = orders.stream()
                                          .filter(o -> o.getEntryType() == EntryType.PAYMENT)
                                          .toList();
      if(ordersByPayment != null)
        transactionPaymentService.setTransactionPayment(ordersByPayment);

      // Working moving to trans line and trans payment :REMOVE-COMMENT
      // Next is move it on Transaction Summary table :REMOVE-COMMENT
      if(orders != null)
        transactionSummaryService.setTransactionSummary(orders);

      orderRepository.deleteAll(orders);
      
      return new OrderPaymentResponse(true, orderMapper.toResponseList(orders));
    }
    
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
