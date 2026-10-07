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

  String firstname;

  String lastname;

  String nameOnReceipt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  UserRole userRole = UserRole.CASHIER;

  @Column(nullable = false, unique = true)
  String username;

  @Column(nullable = false)
  String password;

  @CreationTimestamp 
  private LocalDateTime timeCreated;

  @UpdateTimestamp 
  private LocalDateTime timeUpdated;

  // Called when .save() method is used
  @PrePersist
  public void prePersist(){
    if(userId == null){
        userId = UUID.randomUUID().toString();
    }
  }
}
