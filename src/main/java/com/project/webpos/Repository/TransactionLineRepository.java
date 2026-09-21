package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.webpos.Entity.TransactionLine;

public interface TransactionLineRepository extends JpaRepository<TransactionLine, Long>{

}
