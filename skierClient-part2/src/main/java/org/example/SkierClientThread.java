package org.example;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class SkierClientThread extends Thread {

  private String ip;
  private int port;
  private DataGenerator dataGenerator;
  private int requestsToSend;
  private RequestCounter requestCounter;
  private CountDownLatch latch;
  private List<CSVRecord> records = new ArrayList<>(); // for csv

  private int successCount = 0;

  private int failedCount = 0;

  public SkierClientThread(String ip, int port, DataGenerator dataGenerator, int requestsToSend,
      RequestCounter requestCounter, CountDownLatch latch) {
    this.ip = ip;
    this.port = port;
    this.dataGenerator = dataGenerator;
    this.requestsToSend = requestsToSend;
    this.requestCounter = requestCounter;
    this.latch = latch;
  }

  public int getSuccessCount() {
    return successCount;
  }

  public int getFailedCount() {
    return failedCount;
  }


  public List<CSVRecord> getRecords() {
    return records;
  }

  public void run() {
    // set up
    ApiClient apiClient = new ApiClient();
    SkiersApi apiInstance = new SkiersApi(apiClient);
    apiClient.setBasePath("http://" + ip + ":" + port + "/SkierServer_war_exploded");
    int requestSent = 0;
    // check if queue is empty
    boolean caughtException = false;
    BlockingQueue<LiftRideEvent> queue = dataGenerator.getQueue();
    LiftRideEvent event = null;
    while (requestSent < requestsToSend) {
      if (!caughtException) {
          event = queue.poll();
        }
      caughtException = false;
//        while(dataGenerator.getQueue().isEmpty()){
//        }

      try {
        // send request
//        BlockingQueue<LiftRideEvent> queue = dataGenerator.getQueue();
//        LiftRideEvent event = queue.poll();
//          LiftRideEvent event = dataGenerator.getEvent();
        long startTime = System.currentTimeMillis(); // For CSV writing
        ApiResponse<Void> response = apiInstance.writeNewLiftRideWithHttpInfo(event.getLiftRide(),
            event.getResortID(), event.getSeasonID(), event.getDayID(), event.getSkierID());
        long latency = System.currentTimeMillis() - startTime; // For CSV writing
        records.add(
            new CSVRecord(startTime, "POST", latency, response.getStatusCode())); // For CSV writing
        requestSent++;

        // check response
        if (response.getStatusCode() == 201) {
          successCount += 1;
        }
        // resend request
        else if (response.getStatusCode() >= 400 && response.getStatusCode() < 600) {
          int retryCount = 0;
          boolean isSuccess = false;
          while (retryCount < 5) {
            startTime = System.currentTimeMillis(); // For CSV writing
            ApiResponse<Void> newResponse = apiInstance.writeNewLiftRideWithHttpInfo(
                event.getLiftRide(), event.getResortID(), event.getSeasonID(), event.getDayID(),
                event.getSkierID());
            latency = System.currentTimeMillis() - startTime; // For CSV writing
            records.add(new CSVRecord(startTime, "POST", latency,
                newResponse.getStatusCode())); // For CSV writing
            if (newResponse.getStatusCode() == 201) {
              isSuccess = true;
              break;
            }
            retryCount++;
          }

          // adjust counter
          if (isSuccess == false) {
            failedCount += 1;
          } else {
            successCount += 1;
          }
        }
      } catch (ApiException e) {
        e.printStackTrace();
        caughtException = true;
      }
    }
    latch.countDown();
  }
}
