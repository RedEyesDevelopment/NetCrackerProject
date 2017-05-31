package projectpackage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SearchAvailabilityParamsDTO {
    private Date arrival;
    private Date departure;
    private int livingPersons;
    private int categoryId;
}
