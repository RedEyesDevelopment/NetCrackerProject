package projectpackage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private int roomTypeId;
    private String roomTypeName;
    private String roomTypeDescription;
    private boolean isAvailable;
    private long categoryCost;
    private long livingCost;
    private Date arrival;
    private Date departure;
    private int livingPersons;
    private int categoryId;
    private Integer clientId;
    private Integer roomId;
}
