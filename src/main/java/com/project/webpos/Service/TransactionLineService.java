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
import com.project.webpos.Entity.TransactionLine;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Entity.DTO.SearchRequest;
import com.project.webpos.Entity.DTO.TransactionLineResponse;
import com.project.webpos.Mapper.TransactionLineMapper;
import com.project.webpos.Repository.TransactionLineRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor  
public class TransactionLineService {
  private static final String[] OMIT_FIELDS = {"class", "id"}; 

  private final TransactionLineRepository transactionLineRepository;
  private final TransactionLineMapper transactionLineMapper;
  private final UtilityComponent utilityComponent;

  /**
   * Retrieves the transaction line
   * @param pageable contains the value of {@code Pageable} which specfies the structure of the {@code Page} query
   * @return an object of {@code PageResponse<TransactionSummaryResponse>}
   */
  @Cacheable(value = "transaction-line", key = "#pageable")
  public PagedResponse<TransactionLineResponse> getTransactionLine(Pageable pageable){
    return new PagedResponse<>(transactionLineMapper.toResponseContent(transactionLineRepository.findAll(pageable)));
  }

  /**
   * Retrieves the fields from {@code TransactionSummary} entity and humanizes the field names
   * @return a {@code List<String>} that is in humanized form
   */
  @Cacheable(value = "transaction-line:fields")
  public List<String> getEntityFields(){
    BeanWrapperImpl wrapper = new BeanWrapperImpl(TransactionLine.class);
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
   * @return an object of {@code PageResponse<TransactionSummaryResponse>}
   */
  public PagedResponse<TransactionLineResponse> getTransactionLineSearch(SearchRequest searchRequest, Pageable pageable){
    Specification<TransactionLine> spec = new DynamicSpecification<>(searchRequest.filters());
    return new PagedResponse<>(transactionLineMapper.toResponseContent(transactionLineRepository.findAll(spec, pageable)));
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
