package com.project.webpos.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.project.webpos.Component.DynamicSpecification;
import com.project.webpos.Component.UtilityComponent;
import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.TransactionPayment;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Entity.DTO.SearchRequest;
import com.project.webpos.Entity.DTO.TransactionPaymentResponse;
import com.project.webpos.Mapper.TransactionPaymentMapper;
import com.project.webpos.Repository.TransactionPaymentRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class TransactionPaymentService {
  private static final String[] OMIT_FIELDS = {"class", "id"}; 

  private final TransactionPaymentRepository transactionPaymentRepository;
  private final TransactionPaymentMapper transactionPaymentMapper;
  private final UtilityComponent utilityComponent;

  /**
   * Retrieves the transaction payment
   * @param pageable contains the value of {@code Pageable} which specfies the structure of the {@code Page} query
   * @return an object of {@code PageResponse<TransactionPaymentResponse>}
   */
  public PagedResponse<TransactionPaymentResponse> getTransactionPayment(Pageable pageable){
    return new PagedResponse<>(transactionPaymentMapper.toResponseContent(transactionPaymentRepository.findAll(pageable)));
  }

  /**
   * Retrieves the fields from {@code TransactionPayment} entity and humanizes the field names
   * @return a {@code List<String>} that is in humanized form
   */
  @Cacheable(value = "transaction-payment:fields")
  public List<String> getEntityFields(){
    BeanWrapperImpl wrapper = new BeanWrapperImpl(TransactionPayment.class);
    return Stream.of(wrapper.getPropertyDescriptors())
        .map(descriptor -> {
          int indexOfCapitalizedLetter = utilityComponent.getTheIndexOfTheFirstCapitalizedLetter(descriptor.getName());
          
          return utilityComponent.humanizeFieldNames(indexOfCapitalizedLetter, descriptor.getName());
        })
        .filter(field -> !List.of(OMIT_FIELDS).contains(field.toLowerCase()))
        .toList();
  }

  /**
   * Retrieves the record based on the filter sent by the user
   * @param searchRequest an object of {@code List<FilterRequest>} that contains the dynamic filter to be used
   * @param pageable contains the value of {@code Pageable} which specfies the structure of the {@code Page} query
   * @return an object of {@code PageResponse<TransactionPaymentResponse>}
   */
  public PagedResponse<TransactionPaymentResponse> getTransactionPaymentSearch(SearchRequest searchRequest, Pageable pageable){
    Specification<TransactionPayment> spec = new DynamicSpecification<>(searchRequest.filters());
    return new PagedResponse<>(transactionPaymentMapper.toResponseContent(transactionPaymentRepository.findAll(spec, pageable)));
  }

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
