package com.project.webpos.Enum;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.project.webpos.Exception.InvalidRequestException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public enum FilterOperator {
  EQUALS("="),
  NOT_EQUAL("<>"),
  GREATER_THAN(">"),
  GREATER_THAN_OR_EQUAL(">="),
  LESS_THAN("<"),
  LESS_THAN_OR_EQUAL("<=");

  private final String symbol;

  @JsonCreator 
  public static FilterOperator convertOperatorIntoEnum(String operator){
    return Arrays.stream(values())
        .filter(op -> op.symbol.equalsIgnoreCase(operator))
        .findFirst()
        .orElseThrow(
          () -> new InvalidRequestException("INVALID_OPERATOR", "during converting of operator request into enum", "invalid operator " + operator)
        );
  }
}
