package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.model.orders.Category;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeId = 15)
@ReactChild(outerEntityClass = Category.class, outerFieldName = "complimentaries", innerFieldKey = "categoryId")
public class Complimentary implements ReactEntityWithId {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%PARENT_ID")
    private int categoryId;

    private Maintenance maintenance;

}
