package com.project.webpos.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter
@Setter
public class TenderType {
  @Id
  private String code;
  private String tenderDescription;
  private Double maxTenderAmount;
  private Boolean changeUsed;
  // Add tender type details - meaning it is a general term for it :REMOVE-COMMENT
}
