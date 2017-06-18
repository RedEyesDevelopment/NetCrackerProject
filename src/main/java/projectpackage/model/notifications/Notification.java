package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeName = "Notification")
public class Notification implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Message")
    private String message;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Send_date")
    private Date sendDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Executed_date")
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
