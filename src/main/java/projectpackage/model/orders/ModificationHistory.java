package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;

import java.util.Date;

@Data
@ReactEntity(entityTypeName = "Modification_history")
@ReactChild(outerEntityClass = Order.class, outerFieldName = "historys", outerFieldKey = "objectId", innerFieldKey = "savedOrder")
public class ModificationHistory {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "modifDate")
    private Date modifiDate;

    private User modifAuthor;
    private Order savedOrder;
}
