package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.TransactionSummary;
import com.project.webpos.Enum.EntryType;
import com.project.webpos.Repository.TransactionSummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class TransactionSummaryService {
  private final TransactionSummaryRepository transactionSummaryRepository;

  /**
   * Inserts the summary of the order
   * @param orders contains the {@code List<OrderList>}
   */
  public void setTransactionSummary(List<OrderList> orders){
    Map<Boolean, List<OrderList>> partitioned = orders.stream()
                                                  .collect(Collectors.partitioningBy(o -> o.getEntryType() == EntryType.ORDER));
    List<OrderList> ordersByOrder = partitioned.get(true);
    List<OrderList> ordersByPayment = partitioned.get(false);    

    BigDecimal grossSales = BigDecimal.ZERO;
    BigDecimal netSales = BigDecimal.ZERO;
    BigDecimal vatAmount = BigDecimal.ZERO;
    BigDecimal discountAmt = BigDecimal.ZERO;
    Integer quantity = 0;
    
    BigDecimal payment = ordersByPayment.stream()
                                    .map(OrderList::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

    for(OrderList order : ordersByOrder){
      if(order.getPrice() != null) grossSales = grossSales.add(order.getPrice());
      if(order.getAmount() != null) netSales = netSales.add(order.getAmount());
      if(order.getVatAmount() != null) vatAmount = vatAmount.add(order.getVatAmount());
      if(order.getDiscountAmount() != null) discountAmt = discountAmt.add(order.getDiscountAmount());
      if(order.getQuantity() != null) quantity += order.getQuantity();
    }

    TransactionSummary transactionSummary = new TransactionSummary();
    transactionSummary.setReceiptNo(orders.getFirst().getReceiptNo());
    String transactionNo = getLastTransNo();
    transactionSummary.setTransactionNo(transactionNo);
    transactionSummary.setGrossSales(grossSales);
    transactionSummary.setNetSales(netSales.subtract(vatAmount).subtract(discountAmt));
    transactionSummary.setVatAmount(vatAmount);
    transactionSummary.setDiscountAmount(discountAmt);
    transactionSummary.setPayment(payment);
    transactionSummary.setQuantity(quantity);
    transactionSummaryRepository.save(transactionSummary);
  }

  /**
   * Returns the value of the last Transaction No.
   * @return The value of the Last Transaction No.
   */
  public String getLastTransNo(){
    String transNo = transactionSummaryRepository.findLastTransactionNo();

    if(transNo == null) return "0000000001";
    else{
      Integer incReceipt = Integer.parseInt(transNo);
      incReceipt++;
      // %0 means pad 0 based on the length
      return String.format("%0" + transNo.length() + "d", incReceipt);
    }
  }
}
