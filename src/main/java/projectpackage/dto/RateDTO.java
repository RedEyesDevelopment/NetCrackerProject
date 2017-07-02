package projectpackage.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by Arizel on 02.07.2017.
 */
@Data
public class RateDTO {
    private Integer roomTypeId;
    private Date rateFromDate;
    private Date rateToDate;
    private Long priceForOne;
    private Long priceForTwo;
    private Long priceForThree;
}
