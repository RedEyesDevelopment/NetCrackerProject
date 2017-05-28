package projectpackage.model.maintenances;

import lombok.Data;
import projectpackage.model.orders.Category;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "Complimentary")
@ReactChild(outerEntityClass = Category.class, outerFieldName = "complimentaries", outerFieldKey = "objectId", innerFieldKey = "categoryId")
public class Complimentary implements ReactEntityWithId, Cloneable {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int categoryId;

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
