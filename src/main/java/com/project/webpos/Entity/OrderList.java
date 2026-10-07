package com.project.webpos.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.project.webpos.Enum.EntryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

// Will hold order placed that is still on `Order Summary`
@Entity
@Getter
@Setter
public class OrderList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime transactionDate;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedDate;
  
  @Column(nullable = false)
  private String receiptNo;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EntryType entryType = EntryType.ORDER;

  @Column(nullable = false)
  private Long number;

  @Column(nullable = false)
  private String shortFoodDesc;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private BigDecimal vatAmount = BigDecimal.ZERO;

  private BigDecimal discountAmount = BigDecimal.ZERO;

  private Float discountPercent = Float.valueOf(0);

  private Integer sizeId = null;

  @Column(nullable = false)
  private String size = "None";

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Boolean placed = false; // Means if it is still on the order summary or already placed on a table

  @ManyToOne
  @JoinColumn(name = "table_id")
  private TableList table;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private OrderType orderType;
}
