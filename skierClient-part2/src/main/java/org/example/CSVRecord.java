package org.example;

public class CSVRecord {
  private Long startTime;
  private String requestType;
  private Long latency;
  private Integer responseCode;

  public CSVRecord(Long startTime, String requestType, Long latency, Integer responseCode) {
    this.startTime = startTime;
    this.requestType = requestType;
    this.latency = latency;
    this.responseCode = responseCode;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public Long getLatency() {
    return latency;
  }

  public void setLatency(Long latency) {
    this.latency = latency;
  }

  public Integer getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(Integer responseCode) {
    this.responseCode = responseCode;
  }

  @Override
  public String toString() {
    return startTime.toString() + "," + requestType + "," + latency.toString() + "," + responseCode.toString();
  }
}
