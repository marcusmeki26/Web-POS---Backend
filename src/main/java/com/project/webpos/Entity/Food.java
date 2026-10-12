package com.project.webpos.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Food {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_id_seq")
  @SequenceGenerator(name = "food_id_seq", sequenceName = "food_id_sequence", initialValue = 10000, allocationSize = 1)
  private Long id;
  
  @Column(nullable = false, unique = true)
  private String foodName;

  @Column(nullable = false)
  private String shortFoodDesc;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Boolean withVariant = false;

  @Column(nullable = false)
  private String imageName;

  @OneToMany(mappedBy = "food")
  private List<ProductVariant> prodVariantList; 

  @OneToMany(mappedBy = "food")
  private List<Inventory> inventory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_code")
  private Category category;

  @CreationTimestamp
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @UpdateTimestamp 
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime updatedAt;
}