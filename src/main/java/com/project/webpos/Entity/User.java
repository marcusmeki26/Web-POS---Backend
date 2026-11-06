package com.project.webpos.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.project.webpos.Enum.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class User {
  @Id 
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
  @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", initialValue = 10000, allocationSize = 1)
  Integer id;

  @Column(nullable = false)
  private String userId;

  private String firstname;

  private String lastname;

  private String nameOnReceipt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole userRole = UserRole.CASHIER;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  private String address;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  @CreationTimestamp
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @UpdateTimestamp 
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  // Called when .save() method is used
  @PrePersist
  public void prePersist(){
    if(userId == null){
        userId = UUID.randomUUID().toString();
    }
  }
}
