package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.webpos.Entity.TransactionPayment;

public interface TransactionPaymentRepository extends JpaRepository<TransactionPayment, Integer> {

}
