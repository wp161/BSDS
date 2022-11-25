import Objects.LiftRideDetail;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class DynamoDbProxy {
  private final AmazonDynamoDB ddbClient;
  private final String skierTable = "SkierTable";
  private DynamoDB dynamoDB;

  public DynamoDbProxy(AmazonDynamoDB ddbClient) {
    this.ddbClient = ddbClient;
    this.dynamoDB = new DynamoDB(ddbClient);
  }

  public void insertData(List<LiftRideDetail> details) {
    List<Item> items = new ArrayList<>();
    for(LiftRideDetail detail : details){
      items.add(getItem(detail));
    }
    TableWriteItems skierTableWriteItems = new TableWriteItems(skierTable)
        .withItemsToPut(items);
    BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(skierTableWriteItems);
  }

  private Item getItem(LiftRideDetail detail) {
      String objectId = UUID.randomUUID().toString();
      String skierId = detail.getSkierId();
      String dayId = detail.getDayId();
      String seasonId = detail.getSeasonId();
      Integer resortId = detail.getResortId();
      Integer liftId = detail.getLiftRide().getLiftID();
      Integer time = detail.getLiftRide().getTime();
      Item item = new Item().withPrimaryKey("skierID", skierId, "objectID", objectId)
          .withString("dayID", dayId)
          .withString("seasonID", seasonId)
          .withNumber("resortID", resortId)
          .withNumber("liftID",liftId)
          .withNumber("time", time);
      return item;
  }

  private void addAttribute(Map<String, AttributeValue> item, String attributeName, String value) {
    AttributeValue attributeValue = new AttributeValue(value);
    item.put(attributeName, attributeValue);
  }
}
