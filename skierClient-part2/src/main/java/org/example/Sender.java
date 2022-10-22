package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {
  private int numOfThreads;
  private int totalToSend;
  private RequestCounter counter;
  private DataGenerator dataGenerator;
  private CountDownLatch latch;
  private String ip;
  private int port;

  public Sender(int numOfThreads, int totalToSend, RequestCounter counter,
      DataGenerator dataGenerator, CountDownLatch latch, String ip, int port) {
    this.numOfThreads = numOfThreads;
    this.totalToSend = totalToSend;
    this.counter = counter;
    this.dataGenerator = dataGenerator;
    this.latch = latch;
    this.ip = ip;
    this.port = port;
  }

  public List<Runnable> send(){
    int requestPerThread = totalToSend/ numOfThreads;

//    ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
//    for(int i = 0; i < numOfThreads; i++) {
//      Runnable runner = new SkierClientThread(ip, port, dataGenerator, requestPerThread, counter, latch);
//      executorService.submit(runner);
//    }
    List<Runnable> runnables = new ArrayList<>();
    for(int i = 0; i < numOfThreads; i++){
      Runnable runner = new SkierClientThread(ip, port, dataGenerator, requestPerThread, counter, latch);
      runnables.add(runner);
      new Thread(runner).start();
    }
    return runnables;
  }

  public void createClientThreads(BlockingQueue<Runnable> workQueue) {
    int requestPerThread = totalToSend/ numOfThreads;
    for(int i = 0; i < numOfThreads; i++){
      Runnable runner = new SkierClientThread(ip, port, dataGenerator, requestPerThread, counter, latch);
      workQueue.add(new Thread(runner));
    }
  }

}
