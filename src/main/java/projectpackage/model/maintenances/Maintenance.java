package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "Maintenance")
@ReactReference(referenceName = "MaintenanceToJournalRecord", outerEntityClass = JournalRecord.class,
        outerFieldName = "maintenance", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(referenceName = "MaintenanceToComplimentary", outerEntityClass = Complimentary.class,
        outerFieldName = "maintenance", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Maintenance implements ReactEntityWithId, Cloneable{
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Maintenance_title")
    private String maintenanceTitle;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Maintenance_type")
    private String maintenanceType;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = "Maintenance_price")
    private Long maintenancePrice;

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
