package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;

import java.util.Date;
import java.util.Set;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "JournalRecord")
public class JournalRecord {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;

    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "Count")
    private Integer count;
    @ReactField(valueObjectClass = Long.class, databaseAttrtypeCodeValue = "Cost")
    private Long cost;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Used_date")
    private Date usedDate;

    private Order order;
    private Maintenance maintenance;
}
