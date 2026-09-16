package com.project.webpos.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter 
public enum EntryType {
  ORDER("Order"),PAYMENT("Payment"),VOIDED("Voided");

  private String caption;
}
