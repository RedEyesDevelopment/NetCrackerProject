package projectpackage.model.maintenances;

import lombok.Data;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
public class Maintenance {
    private int objectId;
    private String maintenanceTitle;
    private String maintenanceType;
    private Long maintenancePrice;
}
