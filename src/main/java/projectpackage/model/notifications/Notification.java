package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;

import java.util.Date;

@Data
public class Notification {
    private User author;
    private NotificationType notificationType;
    private String message;
    private Order order;
    private Date sendDate;
    private User executedBy;
    private Date executedDate;
}
