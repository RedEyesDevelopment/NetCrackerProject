package projectpackage.dto;

import lombok.Data;

/**
 * Created by Arizel on 30.06.2017.
 */
@Data
public class NotificationDTO {
    private Integer authorId;
    private Integer notificationTypeId;
    private String message;
    private Integer orderId;
}
