package projectpackage.dto;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class BookedOrderDTO {

    private String roomTypeName;
    private String roomTypeDescription;
    private long categoryCost;
    private long livingCost;
    private Date arrival;
    private Date departure;
    private int livingPersons;
    private String categoryName;
    private String client;

    public BookedOrderDTO(OrderDTO order) {
        this.roomTypeName = order.getRoomTypeName();
        this.roomTypeDescription = order.getRoomTypeDescription();
        this.categoryCost = order.getCategoryCost();
        this.livingCost = order.getLivingCost();
        this.arrival = order.getArrival();
        this.departure = order.getDeparture();
        this.livingPersons = order.getLivingPersons();
    }

}
