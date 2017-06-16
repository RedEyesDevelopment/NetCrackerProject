package projectpackage.dto;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
public class BookedOrderDTO {
    private static final DateFormat DATETIMEFORMATTER = new SimpleDateFormat("HH-mm-ss_MM-dd-yyyy");

    private String roomTypeName;
    private String roomTypeDescription;
    private long categoryCost;
    private long livingCost;
    private String arrival;
    private String departure;
    private int livingPersons;
    private String categoryName;

    public BookedOrderDTO(OrderDTO order) {
        this.roomTypeName = order.getRoomTypeName();
        this.roomTypeDescription = order.getRoomTypeDescription();
        this.categoryCost = order.getCategoryCost();
        this.livingCost = order.getLivingCost();
        this.arrival = DATETIMEFORMATTER.format(order.getArrival());
        this.departure = DATETIMEFORMATTER.format(order.getDeparture());
        this.livingPersons = order.getLivingPersons();
    }

}
