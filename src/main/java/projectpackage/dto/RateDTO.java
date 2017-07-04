package projectpackage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RateDTO {
    private Integer roomTypeId;
    private Date rateFromDate;
    private Date rateToDate;
    private Long priceForOne;
    private Long priceForTwo;
    private Long priceForThree;
}
