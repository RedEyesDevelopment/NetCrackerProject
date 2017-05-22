package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "JournalRecord")
@ReactChild(outerEntityClass = Order.class, outerFieldName = "journalRecords", outerFieldKey = "objectId", innerFieldKey = "categoryId")
public class JournalRecord implements ReactEntityWithId{
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int orderId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "Count")
    private Integer count;
    @ReactField(valueObjectClass = Long.class, databaseAttrtypeCodeValue = "Cost")
    private Long cost;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Used_date")
    private Date usedDate;

    private Maintenance maintenance;
}
