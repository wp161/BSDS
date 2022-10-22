package org.example;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

  public static void main(String[] args) throws InterruptedException, Exception {
    String IP = "54.70.64.15";
    int PORT = 8080;
    int NUMOFREQUEST = 200000;
    int NUMOFTHREADS1 = 32;
    int NUMOFTHREADS2 = 200;
    RequestCounter counter = new RequestCounter();
    DataGenerator dataGenerator = new DataGenerator(NUMOFREQUEST);
    new Thread(dataGenerator).start();
    System.out.println("Generator started");

//    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
//    ThreadPoolExecutor executorPool = new ThreadPoolExecutor(32, 32, 100000, TimeUnit.SECONDS, workQueue);
//    executorPool.prestartAllCoreThreads();


    int totalRequest = NUMOFREQUEST;

    // 1st batch: 32 threads, 1000 request each

//    int requestSent = 32 * 1000;
    int requestSent = NUMOFREQUEST;
    CountDownLatch firstBatchLatch = new CountDownLatch(1);
    Sender firstBatch = new Sender(NUMOFTHREADS1, 32 * 1000, counter,
        dataGenerator, firstBatchLatch, IP, PORT);
    CountDownLatch secondBatchLatch = new CountDownLatch(NUMOFTHREADS2);
    Sender secondBatch = new Sender(NUMOFTHREADS2, totalRequest - NUMOFTHREADS1 * 1000, counter,
        dataGenerator, secondBatchLatch, IP, PORT);

//    Sender firstBatch = new Sender(1, NUMOFREQUEST, counter,
//        dataGenerator, firstBatchLatch, IP, PORT, records);

    System.out.println("Sending first batch");
    long start = System.currentTimeMillis();
    List<Runnable> firstBatchRunnables = firstBatch.send();
//    firstBatch.createClientThreads(workQueue);
    firstBatchLatch.await();
    System.out.println("First thread finished");


    // 2nd batch:
//    executorPool.setMaximumPoolSize(200);
//    executorPool.setCorePoolSize(200);
//    System.out.println("Sending second batch");
//    secondBatch.createClientThreads(workQueue);
//    while(executorPool.getActiveCount() != 0) {
//      System.out.println("Successful request " + counter.getSuccessCount().toString());
//      System.out.println("Size of work queue " + String.valueOf(workQueue.size()));
//      System.out.println("Number of active threads " + String.valueOf(executorPool.getActiveCount()));
//    }
//    executorPool.awaitTermination(10, TimeUnit.MINUTES);
//    secondBatchLatch.await();


    System.out.println("Sending second batch");
    List<Runnable> secondBatchRunnables = secondBatch.send();
    secondBatchLatch.await();
    long end = System.currentTimeMillis();
    System.out.println("Second batch sent");
//    executorPool.shutdownNow();

    long timeTaken = (end - start) / 1000;


    // Data Aggregation
    int totalSuccessCount = 0;
    int totalFailedCount = 0;
    List<CSVRecord> records = new ArrayList<>();

    for (Runnable runnable : firstBatchRunnables) {
      totalSuccessCount += ((SkierClientThread) runnable).getSuccessCount();
      totalFailedCount +=  ((SkierClientThread) runnable).getFailedCount();
      records.addAll(((SkierClientThread) runnable).getRecords());
    }

    for (Runnable runnable : secondBatchRunnables) {
      totalSuccessCount += ((SkierClientThread) runnable).getSuccessCount();
      totalFailedCount +=  ((SkierClientThread) runnable).getFailedCount();
      records.addAll(((SkierClientThread) runnable).getRecords());
    }


    System.out.println("Number of threads used: " + "First batch-" + NUMOFTHREADS1 +
        " threads. Second batch-" + NUMOFTHREADS2 + " threads");
    System.out.println("Number of successful requests sent： " + String.valueOf(totalSuccessCount));
    System.out.println("Number of unsuccessful requests： " + String.valueOf(totalFailedCount));
    System.out.println("The total run time (wall time) for all phases to complete： " + timeTaken);
    System.out.println("The total throughput in requests per second： " + ((double) totalRequest / timeTaken));

    writeToCSV(records);

    long[] responseTime = new long[NUMOFREQUEST];
    for(int i = 0; i < NUMOFREQUEST; i++){
      responseTime[i] = records.get(i).getLatency();
    }

    Long sum = (long)0;
    Long median;
    Long min = responseTime[0];
    Long max = responseTime[0];
    Long p99 = responseTime[(int) Math.ceil(99 / 100.0 * responseTime.length) - 1];

    for(Long num : responseTime){
      sum += num;
      if(num < min) min = num;
      if(num > max) max = num;
    }
    Long avg = sum / NUMOFREQUEST;

    Arrays.sort(responseTime);
    if(responseTime.length % 2 == 0){
      median = ((long) responseTime[responseTime.length / 2] +
          (long)responseTime[responseTime.length / 2 - 1])/2;
    } else{
      median = (long) responseTime[responseTime.length / 2];
    }

    System.out.println("Mean response time: " + avg);
    System.out.println("Median response time: " + median);
    System.out.println("99th percentile response time: " + p99);
    System.out.println("Min response time: " + min);
    System.out.println("Max response time: " + max);

  }

  private static void writeToCSV(List<CSVRecord> records) throws Exception {
    System.out.println("Size of CSV records " + String.valueOf(records.size()));
    Writer writer = new FileWriter("output.csv");
    StatefulBeanToCsv<CSVRecord> sbc = new StatefulBeanToCsvBuilder<CSVRecord>(writer)
        .withQuotechar('\'')
        .withSeparator(',')
        .build();
    sbc.write(records);
    writer.close();
  }
}