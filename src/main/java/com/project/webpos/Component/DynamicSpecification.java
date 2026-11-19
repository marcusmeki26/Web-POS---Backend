package com.project.webpos.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.project.webpos.Entity.DTO.FilterRequest;
import com.project.webpos.Exception.InvalidRequestException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DynamicSpecification<T> implements Specification<T> {
  private final List<FilterRequest> filters;

  @Override
  @Nullable 
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    List<Predicate> predicates = new ArrayList<>();

    for(FilterRequest filter : filters){
      Path<?> path = root.get(filter.field());

      Object value = convertValue(path.getJavaType(), filter.value());

      Predicate predicate = switch(filter.operator()){
        case EQUALS -> cb.equal(path, value);
        case NOT_EQUAL -> cb.notEqual(path, value);
        case GREATER_THAN -> greaterThan(cb, path, value);
        case GREATER_THAN_OR_EQUAL -> greaterThanOrEqual(cb, path, value);
        case LESS_THAN -> lessThan(cb, path, value);
        case LESS_THAN_OR_EQUAL -> lessThanOrEqual(cb, path, value);
        default ->
          throw new InvalidRequestException(
            "INVALID_OPERATOR", 
            "during generating a specification",
            "Operator is not supported");
      };

      predicates.add(predicate);
    }

    return cb.and(predicates.toArray(new Predicate[0]));
  }

  private Object convertValue(Class<?> pathType, String value){
    if(pathType.isInstance(value))
      return value;
    else if(pathType == Integer.class || pathType == int.class)
      return Integer.parseInt(value);
    else if(pathType == BigDecimal.class)
      return new BigDecimal(value);
    else if(pathType == LocalDateTime.class)
      return LocalDateTime.parse(value);
    else{
      throw new InvalidRequestException(
        "INVALID_VALUE", 
        "during conversion of filter", 
        "Invalid value type"
      );
    } 
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Predicate greaterThan(CriteriaBuilder cb, Path<?> path, Object value){
    return cb.greaterThan((Expression) path, (Comparable) value);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Predicate greaterThanOrEqual(CriteriaBuilder cb, Path<?> path, Object value){
    return cb.greaterThanOrEqualTo((Expression) path, (Comparable) value);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Predicate lessThan(CriteriaBuilder cb, Path<?> path, Object value){
    return cb.lessThan((Expression) path, (Comparable) value);
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  private Predicate lessThanOrEqual(CriteriaBuilder cb, Path<?> path, Object value){
    return cb.lessThanOrEqualTo((Expression) path, (Comparable) value);
  }
}
