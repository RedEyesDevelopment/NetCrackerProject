package projectpackage.model.auth;

import lombok.Data;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeId = 10)
@ReactReference(referenceName = "RoleToUser", outerEntityClass = User.class, outerFieldName = "role", attrIdField = "20")
@ReactReference(referenceName = "RoleToNotificationType", outerEntityClass = NotificationType.class, outerFieldName = "orientedRole")
public class Role implements ReactEntityWithId {

    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 39)
    private String roleName;

}
