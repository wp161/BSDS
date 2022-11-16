import Objects.LiftRideDetail;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import java.util.HashMap;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

public class MessageReader implements Runnable{

  private AmazonSQS sqs;
  private String queueName;
  private Map<String, LiftRideDetail> map = new HashMap<String, LiftRideDetail>();

  public MessageReader(AmazonSQS sqs, String queueName) {
    this.sqs = sqs;
    this.queueName = queueName;
  }
  /**
   * When an object implementing interface {@code Runnable} is used to create a thread, starting the
   * thread causes the object's {@code run} method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method {@code run} is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
    while (true) {
      List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
      for (Message m: messages) {
        sqs.deleteMessage(queueUrl, m.getReceiptHandle());
        String msgBody = m.getBody();
        System.out.println(msgBody);
        LiftRideDetail detail = new Gson().fromJson(msgBody, LiftRideDetail.class);
        this.map.put(m.getMessageId(), detail);
      }
    }
  }
}
