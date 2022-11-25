import Objects.LiftRide;
import Objects.LiftRideDetail;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerRunnable implements Runnable{
  private AmazonSQS sqs;
  private String queueUrl;
  private DynamoDbProxy ddbProxy;
  private Map<String, List<LiftRideDetail>> map = new HashMap<String, List<LiftRideDetail>>();
  private boolean done = false;

  public ConsumerRunnable(AmazonSQS sqs, String url, DynamoDbProxy ddbProxy) {
    this.sqs = sqs;
    this.queueUrl = url;
    this.ddbProxy = ddbProxy;
  }


  public Map<String, List<LiftRideDetail>> getMap() {
    return map;
  }

  public void markAsDone() {
    done = true;
  }

  public void run() {
    Long threadId = Thread.currentThread().getId();
//    System.out.println("Consumer with thread id " + threadId.toString() + " started");
    while (!done) {
      ReceiveMessageRequest request = new ReceiveMessageRequest()
          .withQueueUrl(queueUrl)
          .withVisibilityTimeout(20)
          .withMaxNumberOfMessages(10);
      List<Message> messages = sqs.receiveMessage(request).getMessages();
      List<DeleteMessageBatchRequestEntry> entries = new ArrayList<DeleteMessageBatchRequestEntry>();
      List<LiftRideDetail> details = new ArrayList<>();
      for (Message message: messages) {
        entries.add(new DeleteMessageBatchRequestEntry().withId(UUID.randomUUID().toString())
            .withReceiptHandle(message.getReceiptHandle()));
        Map<String, MessageAttributeValue> attributes = message.getMessageAttributes();
        LiftRideDetail detail = new Gson().fromJson(message.getBody(), LiftRideDetail.class);
//        List<LiftRideDetail> details = map.getOrDefault(detail.getSkierId(), new ArrayList<LiftRideDetail>());
        details.add(detail);
//        map.put(detail.getSkierId(), details);
      }

      if (details.size() > 0) {
        ddbProxy.insertData(details);
      }

      if (entries.size() > 0) {
        DeleteMessageBatchRequest request1 = new DeleteMessageBatchRequest()
            .withQueueUrl(queueUrl)
            .withEntries(entries);
        sqs.deleteMessageBatch(request1);
      }
    }
  }

}