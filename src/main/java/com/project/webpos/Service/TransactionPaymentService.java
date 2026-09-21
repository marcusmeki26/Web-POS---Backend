package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.TransactionPayment;
import com.project.webpos.Repository.TransactionPaymentRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class TransactionPaymentService {
  private final TransactionPaymentRepository transactionPaymentRepository;

  /**
   * Inserts entry type = PAYMENT into Transaction Payment table
   * @param orders contains the {@code List<OrderList>} in which the entry type = PAYMENT
   */
  public void setTransactionPayment(List<OrderList> orders){
    orders.forEach(order -> {
      TransactionPayment transactionPayment = new TransactionPayment();
      transactionPayment.setReceiptNo(order.getReceiptNo());
      transactionPayment.setNumber(order.getNumber());
      transactionPayment.setAmountTendered(order.getAmount());
      // Add here that checks from tender type table if it is a card :REMOVE-COMMENT
      // transactionPayment.setAmountTendered(null);
      if(transactionPayment.getAmountTendered().compareTo(BigDecimal.valueOf(0)) == -1)
        transactionPayment.setChangeLine(true);
      else
        transactionPayment.setChangeLine(false);


      transactionPayment.setTable(order.getTable());

      transactionPaymentRepository.save(transactionPayment);
    });
  }
}
