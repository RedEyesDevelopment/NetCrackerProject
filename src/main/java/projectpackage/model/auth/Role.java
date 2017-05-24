package projectpackage.model.auth;

import lombok.Data;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Role")
@ReactReference(referenceName = "RoleToUser", outerEntityClass = User.class, outerFieldName = "role", outerFieldKey = "objectId", innerFieldKey = "objectId", attrIdField = "20")
@ReactReference(referenceName = "RoleToNotificationType", outerEntityClass = NotificationType.class, outerFieldName = "orientedRole", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Role implements ReactEntityWithId, Cloneable {

    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Role_name")
    private String roleName;

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
