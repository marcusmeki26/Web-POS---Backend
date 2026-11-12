package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.TransactionSummary;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Entity.DTO.TransactionSummaryResponse;
import com.project.webpos.Entity.DTO.TransactionSummaryFilter;
import com.project.webpos.Enum.EntryType;
import com.project.webpos.Mapper.TransactionSummaryMapper;
import com.project.webpos.Repository.TransactionSummaryRepository;
import com.project.webpos.Specification.TransactionSummarySpecificiation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class TransactionSummaryService {
  private static final String[] OMIT_FIELDS = {"class", "id"}; 

  private final TransactionSummaryRepository transactionSummaryRepository;
  private final TransactionSummaryMapper transactionSummaryMapper;

  /**
   * Retrieves the transaction
   * @param pageable contains the value of {@code Pageable} which specfies the structure of the {@code Page} query
   * @return an object of {@code PageResponse<TransactionSummaryResponse>}
   */
  @Cacheable(
    value = "transaction-summary",
    condition = "#transactionSummaryFilter == null"
  )
  public PagedResponse<TransactionSummaryResponse> getTransactionSummary(TransactionSummaryFilter transactionSummaryFilter, Pageable pageable){
    if(transactionSummaryFilter != null){
      Specification<TransactionSummary> spec = Specification
          .where(TransactionSummarySpecificiation.hasCashierId(transactionSummaryFilter.getCashierId()))
          .and(TransactionSummarySpecificiation.hasReceiptNo(transactionSummaryFilter.getReceiptNo()))
          .and(TransactionSummarySpecificiation.hasTransactionNo(transactionSummaryFilter.getTransactionNo()));

      return new PagedResponse<>(transactionSummaryMapper.toResponseContent(transactionSummaryRepository.findAll(spec, pageable)));
    }

    return new PagedResponse<>(transactionSummaryMapper.toResponseContent(transactionSummaryRepository.findAll(pageable)));
  }

  /**
   * Retrieves the fields from {@code TransactionSummary} entity and transforms the text into Pascal Case
   * @return an object of {@code List<String>} that contains the transformed fields.
   */
  public List<String> getEntityFields(){
    BeanWrapperImpl wrapper = new BeanWrapperImpl(TransactionSummary.class);
    return Stream.of(wrapper.getPropertyDescriptors())
        .map(descriptor -> {
          String descriptorName = descriptor.getName();

          int indexOfCapitalizedLetter = getTheIndexOfTheFirstCapitalizedLetter(descriptorName); 
          
          if(indexOfCapitalizedLetter == -1)
            return Character.toUpperCase(descriptorName.charAt(0)) + descriptorName.substring(1, descriptorName.length());
        
          return Character.toUpperCase(descriptorName.charAt(0)) + descriptorName.substring(1, indexOfCapitalizedLetter) + ' ' + descriptorName.substring(indexOfCapitalizedLetter);
        })
        .filter(field -> !List.of(OMIT_FIELDS).contains(field.toLowerCase()))
        .toList();
  }

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

  // =======================================================
  // UTILITIES
  // =======================================================
  private int getTheIndexOfTheFirstCapitalizedLetter(String word){
    for(int i = 0; i < word.length(); i++){
      if(Character.isUpperCase(word.charAt(i))){
        return i;
      }
    }

    return -1;
  }
}
