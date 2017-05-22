package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "Complimentary")
public class Complimentary implements ReactEntityWithId{
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;

    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int categoryId;

    private Maintenance maintenance;
}
