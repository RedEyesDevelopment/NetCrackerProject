package projectpackage.model.maintenances;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
public class JournalRecord {
    private int objectId;
    private Integer orderId;
    private Set<Maintenance> maintenances;
    private Integer count;
    private Long cost;
    private Date usedDate;
}
