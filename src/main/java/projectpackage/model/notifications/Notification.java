package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeId = 4)
public class Notification implements ReactEntityWithId, Cloneable {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 22)
    private String message;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 23)
    private Date sendDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 25)
    private Date executedDate;

    private User author;
    private Order order;
    private User executedBy;
    private NotificationType notificationType;

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
