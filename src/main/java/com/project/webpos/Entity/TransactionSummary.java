package com.project.webpos.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TransactionSummary {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trans_sum_id_seq")
  @SequenceGenerator(name = "trans_sum_id_seq", sequenceName = "trans_sum_id_sequence", initialValue = 10000, allocationSize = 1)
  private Long id;

  // Receipt No should appear at the UI
  @Column(nullable = false)
  private String receiptNo;

  // We should increment this manually before insertion
  @Column(nullable = false)
  private String transactionNo;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime transactionDate;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal grossSales;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal netSales;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal discountAmount;
  
  @Column(nullable = false)
  private Integer quantity;
}
