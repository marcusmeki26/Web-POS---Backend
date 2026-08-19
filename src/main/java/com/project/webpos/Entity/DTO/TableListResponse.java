package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableListResponse {
  private Integer id;
  private Integer noOfSeats;
  private String tableName;
}
