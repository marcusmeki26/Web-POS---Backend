package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.TransactionLine;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Entity.DTO.TransactionLineResponse;
import com.project.webpos.Mapper.TransactionLineMapper;
import com.project.webpos.Repository.TransactionLineRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor  
public class TransactionLineService {
  private final TransactionLineRepository transactionLineRepository;
  private final TransactionLineMapper transactionLineMapper;

  /**
   * Retrieves the transaction line
   * @param pageable contains the value of {@code Pageable} which specfies the structure of the {@code Page} query
   * @return an object of {@code PageResponse<TransactionSummaryResponse>}
   */
  @Cacheable(value = "transaction-line", key = "#pageable")
  public PagedResponse<TransactionLineResponse> getTransactionLine(Pageable pageable){
    return new PagedResponse<>(transactionLineMapper.toResponsePage(transactionLineRepository.findAll(pageable)));
  }
  
  /**
   * Inserts entry type = ORDER into Transaction Line table
   * @param orders contains the {@code List<OrderList>} in which the entry type = ORDER
   */
  public void setTransactionLine(List<OrderList> orders){
    orders.forEach(order -> {
      TransactionLine transactionLine = new TransactionLine();
      transactionLine.setReceiptNo(order.getReceiptNo());
      transactionLine.setGrossAmount(order.getPrice().negate());
      transactionLine.setNetAmount(order.getAmount().negate());
      transactionLine.setVatAmount(BigDecimal.valueOf(0));
      transactionLine.setDiscountAmount(order.getDiscountAmount());
      transactionLine.setQuantity(-order.getQuantity());
      transactionLine.setNumber(order.getNumber());
      transactionLine.setShortFoodDesc(order.getShortFoodDesc());
      transactionLine.setPrice(order.getPrice());
      transactionLine.setSize(order.getSize());
      transactionLine.setSizeId(order.getSizeId());
      transactionLine.setTable(order.getTable());

      transactionLineRepository.save(transactionLine);
    });
  }
}
