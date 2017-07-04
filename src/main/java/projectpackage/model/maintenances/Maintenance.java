package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeId = 14)
@ReactReference(referenceName = "MaintenanceToJournalRecord", outerEntityClass = JournalRecord.class,
        outerFieldName = "maintenance")
@ReactReference(referenceName = "MaintenanceToComplimentary", outerEntityClass = Complimentary.class,
        outerFieldName = "maintenance")
public class Maintenance implements ReactEntityWithId {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 47)
    private String maintenanceTitle;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 48)
    private String maintenanceType;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 49)
    private Long maintenancePrice;

}
