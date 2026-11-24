package com.project.webpos.Record;

public record CompanyInformationResponse(
  String companyName,
  String companyAddress,
  String companyContactNo,
  String companyEmail
) {
}
