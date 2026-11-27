package com.project.webpos.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.webpos.Entity.StoreInformation;
import com.project.webpos.Record.CompanyInformationResponse;
import com.project.webpos.Record.TaxPercentResponse;

public interface StoreInformationRepository extends JpaRepository<StoreInformation, Integer> {
  Optional<StoreInformation> findFirstBy();
  Optional<TaxPercentResponse> findTaxPercentBy();
  Optional<CompanyInformationResponse> findCompanyInformationBy();
}
