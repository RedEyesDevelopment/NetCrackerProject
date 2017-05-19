package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.maintenances.Complimentary;

import java.util.Set;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
public class Category {
    private int objectId;
    private String categoryTitle;
    private String categoryPrice;
    private Set<Complimentary> complimentaries;
}
