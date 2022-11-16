import Objects.LiftRide;
import Objects.LiftRideDetail;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import com.google.gson.Gson;

public class RecieverCallback implements MessageListener {
  private Map<String, LiftRideDetail> map = new HashMap<String, LiftRideDetail>();

  public RecieverCallback() {
  }

  public Map<String, LiftRideDetail> getMap() {
    return map;
  }

  @Override
  public void onMessage(Message message) {
    try {
      String msgbody = ((TextMessage) message).getText();
//      msgbody = "{" + msgbody;
      Gson gson = new Gson();
      LiftRideDetail detail = gson.fromJson(msgbody, LiftRideDetail.class);
      map.put(message.getJMSMessageID(), detail);
      // System.out.println("Recived body " + msgbody);

    } catch (JMSException e) {
      e.printStackTrace();
    }
  }
}
