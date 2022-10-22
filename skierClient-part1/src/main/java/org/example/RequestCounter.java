package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestCounter {
  private AtomicInteger successCount = new AtomicInteger(0);
  private AtomicInteger failedCount = new AtomicInteger(0);

  public AtomicInteger getSuccessCount() {
    return successCount;
  }

  public AtomicInteger getFailedCount() {
    return failedCount;
  }

  public void incrementSuccess() {
    successCount.incrementAndGet();
  }

  public void incrementFailed() {
    failedCount.incrementAndGet();
  }
}
