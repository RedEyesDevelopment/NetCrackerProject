package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.annotations.ReactChild;
import projectpackage.repository.reacdao.annotations.ReactEntity;
import projectpackage.repository.reacdao.annotations.ReactField;
import projectpackage.repository.reacdao.model.ReactEntityWithId;

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
