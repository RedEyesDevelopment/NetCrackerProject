package projectpackage.model.rates;

import lombok.Data;
import projectpackage.model.rooms.RoomType;

import java.util.Date;

@Data
public class Rate {
    private Date rateFromDate;
    private Date rateToDate;
    private RoomType roomType;
}
