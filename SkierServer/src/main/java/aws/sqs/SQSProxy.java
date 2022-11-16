package aws.sqs;

import Objects.LiftRideDetail;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import utilities.JsonUtilities;

public class SQSProxy {
  private AmazonSQS sqs;
  public SQSProxy() {
    sqs = AmazonSQSClientBuilder.defaultClient();
  }


  public void sendData(LiftRideDetail detail) {
    SendMessageRequest send_msg_request = new SendMessageRequest()
        .withQueueUrl(Constants.SQS_QUEUE_URL)
        .withMessageBody(JsonUtilities.toJsonString(detail));

    sqs.sendMessage(send_msg_request);
  }
}
