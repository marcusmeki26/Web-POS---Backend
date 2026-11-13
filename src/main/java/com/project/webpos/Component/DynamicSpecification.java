package com.project.webpos.Component;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.project.webpos.Entity.DTO.FilterRequest;
import com.project.webpos.Exception.InvalidRequestException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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

      Predicate predicate = switch(filter.operator()){
        case EQUALS -> cb.equal(path, filter.value());
        case NOT_EQUAL -> cb.notEqual(path, filter.value());
        // case GREATER_THAN -> cb.greaterThan();
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
}
