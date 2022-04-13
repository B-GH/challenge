package com.cirestechnologies.challenge.api.payload.response;

public class BatchResponse {
  private String totalRecords;
  private String successfullyImported;
  private String notImported;

  public BatchResponse(int totalRecords, long successfullyImported, long notImported) {
    this.totalRecords = String.valueOf(totalRecords);
    this.successfullyImported = String.valueOf(successfullyImported);
    this.notImported = String.valueOf(notImported);
  }

  public String getTotalRecords() {
    return totalRecords;
  }

  public void setTotalRecords(String totalRecords) {
    this.totalRecords = totalRecords;
  }

  public String getSuccessfullyImported() {
    return successfullyImported;
  }

  public void setSuccessfullyImported(String successfullyImported) {
    this.successfullyImported = successfullyImported;
  }

  public String getNotImported() {
    return notImported;
  }

  public void setNotImported(String notImported) {
    this.notImported = notImported;
  }
}
