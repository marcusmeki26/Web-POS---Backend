package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.TaxDetail;

@Repository
public interface TaxDetailRepository extends JpaRepository<TaxDetail, Integer>{
  TaxDetail findByIsActive(Boolean isActive);
}
