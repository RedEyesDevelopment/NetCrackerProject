package projectpackage.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Integer authorId;
    private Integer notificationTypeId;
    private String message;
    private Integer orderId;
}
