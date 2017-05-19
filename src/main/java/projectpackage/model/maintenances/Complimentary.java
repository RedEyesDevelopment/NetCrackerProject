package projectpackage.model.maintenances;

import lombok.Data;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
public class Complimentary {
    private int objectId;
    private String categoryTitle;
    private Long categoryPrice;
    private Maintenance maintenance;
}
