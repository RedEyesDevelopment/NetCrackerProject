package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;

import java.util.Date;

@Data
public class ModificationHistory {
    private User modifAuthor;
    private Date modifiDate;
    private Order savedOrder;
}
