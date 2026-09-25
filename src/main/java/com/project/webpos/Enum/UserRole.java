package com.project.webpos.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor 
@Getter 
public enum UserRole {
  CASHIER("CASHIER"),MANAGER("MANAGER"),OWNER("OWNER");

  private String caption;
}
