package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "JournalRecord")
@ReactChild(outerEntityClass = Order.class, outerFieldName = "journalRecords", outerFieldKey = "objectId", innerFieldKey = "orderId")
public class JournalRecord implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%PARENT_ID")
    private int orderId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "Count")
    private Integer count;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = "Cost")
    private Long cost;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Used_date")
    private Date usedDate;

    private Maintenance maintenance;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
