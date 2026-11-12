package com.project.webpos.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.project.webpos.Entity.TransactionSummary;
import com.project.webpos.Entity.TransactionSummary_;

public final class TransactionSummarySpecificiation {

  private TransactionSummarySpecificiation(){}

  public static Specification<TransactionSummary> hasCashierId(Integer cashierId){
    return (root, query, cb) -> cashierId == null ? null : cb.equal(root.get(TransactionSummary_.cashierId), cashierId);
  }

  public static Specification<TransactionSummary> hasReceiptNo(String receiptNo){
    return (root, query, cb) -> receiptNo == null ? null : cb.equal(root.get(TransactionSummary_.receiptNo), receiptNo);
  }

  public static Specification<TransactionSummary> hasTransactionNo(String transactionNo){
    return (root, query, cb) -> transactionNo == null ? null : cb.equal(root.get(TransactionSummary_.transactionNo), transactionNo);
  }
}