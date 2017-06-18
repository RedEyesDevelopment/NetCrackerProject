package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.Role;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Notification_type")
@ReactReference(referenceName = "NotificationTypeToNotification", outerEntityClass = Notification.class, outerFieldName = "notificationType", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class NotificationType implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Notif_type_title")
    private String notificationTypeTitle;

    private Role orientedRole;

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
