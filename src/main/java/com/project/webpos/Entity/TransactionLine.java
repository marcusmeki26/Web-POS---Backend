package com.project.webpos.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TransactionLine {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trans_sum_id_seq")
  @SequenceGenerator(name = "trans_sum_id_seq", sequenceName = "trans_sum_id_sequence", initialValue = 10000, allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String receiptNo;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime transactionDate;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal grossAmount;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal netAmount;
  
  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal vatAmount;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal discountAmount;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Long number;

  @Column(nullable = false)
  private String shortFoodDesc;

  @Column(nullable = false)
  private BigDecimal price;

  private Integer sizeId = null;

  @Column(nullable = false)
  private String size = "None";
  
  @ManyToOne
  @JoinColumn(name = "table_id")
  private TableList table;
  
  @CreationTimestamp
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @UpdateTimestamp 
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime updatedAt;
}
