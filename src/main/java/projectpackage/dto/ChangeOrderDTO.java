package projectpackage.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by Arizel on 01.07.2017.
 */
@Data
public class ChangeOrderDTO {
    private Integer numberOfResidents;
    private Integer roomTypeId;
    private Integer categoryId;
    private Date livingStartDate;
    private Date livingFinishDate;
    private Integer roomId;
}
