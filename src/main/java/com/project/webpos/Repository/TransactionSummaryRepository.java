package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.TransactionSummary;

@Repository
public interface TransactionSummaryRepository extends JpaRepository<TransactionSummary,Long> {
  @Query("SELECT ts " + 
         "FROM TransactionSummary ts " +
         "ORDER BY ts.receiptNo DESC " +
         "LIMIT 1")
  TransactionSummary findCurrentReceiptNo();

  @Query ("SELECT ts.transactionNo " +
          "FROM  TransactionSummary ts " + 
          "WHERE ts.transactionNo != '' " +
          "ORDER BY ts.receiptNo DESC " + 
          "LIMIT 1")
  String findLastTransactionNo();
}
