package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.Role;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;

@Data
@ReactEntity(entityTypeName = "Notification_type")
@ReactReference(outerEntityClass = Notification.class, outerFieldName = "notificationType", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class NotificationType {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Notif_type_title")
    private String notificationTypeTitle;

    private Role orientedRole;
}
