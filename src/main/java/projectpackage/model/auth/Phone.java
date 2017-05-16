package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Phone")
@ReactChild(outerEntityClass = User.class, outerFieldName = "phones", outerFieldKey = "objectId", innerFieldKey = "userId")
public class Phone implements ReactEntityWithId {

    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int userId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Phone_number")
    private String phoneNumber;

}
