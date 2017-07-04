package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.Role;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeId = 11)
@ReactReference(referenceName = "NotificationTypeToNotification", outerEntityClass = Notification.class, outerFieldName = "notificationType")
public class NotificationType implements ReactEntityWithId {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 40)
    private String notificationTypeTitle;

    private Role orientedRole;

}
