package org.example;

import io.swagger.client.model.LiftRide;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class DataGenerator extends Thread {
  private BlockingQueue<LiftRideEvent> queue = new LinkedBlockingQueue<>();
  private LiftRideEvent event;
  private int numOfEvents;
  private int MAX_QUEUE_SIZE = 1000000;

  public DataGenerator(int size) {
    this.numOfEvents = size;
  }

  public BlockingQueue<LiftRideEvent> getQueue() {
    return queue;
  }


  public void run() {
    System.out.println("Generating data");
    for(int i = 0; i < numOfEvents; i++){
      while (queue.size() == MAX_QUEUE_SIZE) {
      }
      // Math.random() not thread-safe??
      Integer skierID = 1 + (int)(Math.random() * ((100000 - 1) + 1));
      Integer resortID = 1 + (int)(Math.random() * ((10 - 1) + 1));
      Integer liftID = 1 + (int)(Math.random() * ((40 - 1) + 1));
      String seasonID = "2022";
      String dayID = "1";
      Integer time = 1 + (int)(Math.random() * ((360 - 1) + 1));
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(liftID);
      liftRide.setTime(time);
      event = new LiftRideEvent(liftRide, skierID, resortID, seasonID, dayID);
      queue.offer(event);
    }
    System.out.println("Data generation completed");
  }
  // q1: wait() & notifyAll() for consumer/producer?
  // q2: enforce the data generator
  // q3: design doc: classes - uml? packages - ??? relationship - uml?
  // q4: client design practices?
  //      part 1 - best throughput?
  //      part 2 - throughput within 5%?
  //      tricks for fastest client?
}
