package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "Maintenance")
@ReactReference(referenceName = "MaintenanceToJournalRecord", outerEntityClass = JournalRecord.class,
        outerFieldName = "maintenance", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(referenceName = "MaintenanceToComplimentary", outerEntityClass = Complimentary.class,
        outerFieldName = "maintenance", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Maintenance {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Maintenance_title")
    private String maintenanceTitle;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Maintenance_type")
    private String maintenanceType;
    @ReactField(valueObjectClass = Long.class, databaseAttrtypeCodeValue = "Maintenance_price")
    private Long maintenancePrice;
}
