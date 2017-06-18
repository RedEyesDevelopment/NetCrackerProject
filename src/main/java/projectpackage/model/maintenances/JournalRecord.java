package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "JournalRecord")
@ReactChild(outerEntityClass = Order.class, outerFieldName = "journalRecords", outerFieldKey = "objectId", innerFieldKey = "orderId")
public class JournalRecord implements ReactEntityWithId, Cloneable {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%PARENT_ID")
    private int orderId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = 54)
    private Integer count;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 55)
    private Long cost;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 56)
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
