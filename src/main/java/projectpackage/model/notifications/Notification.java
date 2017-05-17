package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeName = "Notification")
public class Notification implements ReactEntityWithId {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Message")
    private String message;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Send_date")
    private Date sendDate;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Executed_date")
    private Date executedDate;

    private User author;
    private Order order;
    private User executedBy;
    private NotificationType notificationType;
}
