package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.rooms.Room;

import java.util.Date;

@Data
public class Order {
    private Integer orderId;
    private Room room;
    private User client;
    private Date registrationDate;
    private Boolean isPaidFor;
    private Boolean isConfirmed;
    private Date livingStartDate;
    private Date livingFinishDate;
    private Long sum;
    private String comment;
}
