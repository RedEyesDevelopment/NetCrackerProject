package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.annotations.ReactEntity;
import projectpackage.repository.reacdao.annotations.ReactField;
import projectpackage.repository.reacdao.annotations.ReactReference;
import projectpackage.repository.reacdao.model.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Role")
@ReactReference(outerEntityClass = User.class, outerFieldName = "role", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Role implements ReactEntityWithId {

    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Role_name")
    private String roleName;

}
