package com.project.webpos.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter 
public class StoreInformation {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String companyName = "Web POS";
  private String companyAddress;
  private String companyContactNo;
  private String companyEmail;
  @Column(nullable = false)
  private Double taxPercent = 12.00;
  @UpdateTimestamp
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") 
  private LocalDateTime updatedAt;
}
