import Objects.LiftRide;
import Objects.LiftRideDetail;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
  private static ConcurrentHashMap<String, LiftRideDetail> map = new ConcurrentHashMap<String, LiftRideDetail>();
  public static void main(String[] args) throws Exception {

    String queueName = "LiftEventStandardQueue";
    final ClientConfiguration clientConfiguration = new ClientConfiguration()
        .withMaxConnections(1000);

    final AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
        .withClientConfiguration(clientConfiguration)
        .withRegion("us-west-2")
        .build();

    final String queueUrl = sqsClient
        .getQueueUrl(new GetQueueUrlRequest(queueName)).getQueueUrl();

    int consumerCount = 550;
    List<ConsumerRunnable> consumers = new ArrayList<>();

    for (int i = 0; i < consumerCount; i++) {
      ConsumerRunnable consumer = new ConsumerRunnable(sqsClient, queueUrl);
      new Thread(consumer).start();
      consumers.add(consumer);
    }

    // Start the monitor thread.
    // Wait for the specified amount of time then stop.
    Thread.sleep(1000 * 60 * 5); // run for five minutes

    // assume all consumers are done

    for (ConsumerRunnable consumer: consumers) {
      consumer.markAsDone();
    }

    Thread.sleep(1000 * 60); // wait for all threads to stop

    Map<String, List<LiftRideDetail>> map = new HashMap<String, List<LiftRideDetail>>();
    for(ConsumerRunnable consumer: consumers) {
      for(String skierId: consumer.getMap().keySet()) {
        List<LiftRideDetail> details = map.getOrDefault(skierId, new ArrayList<LiftRideDetail>());
        details.addAll(consumer.getMap().get(skierId));
        map.put(skierId, details);
      }
    }
//    Thread.sleep(1000 * 10); // wait 10 seconds for the threads to finish

    sqsClient.shutdown();
    System.exit(0);
  }
}
