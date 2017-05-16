package projectpackage.model.notifications;

import lombok.Data;
import projectpackage.model.auth.Role;

@Data
public class NotificationType {
    private String notificationTypeTitle;
    private Role orientedRole;
}
