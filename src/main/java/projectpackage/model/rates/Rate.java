package projectpackage.model.rates;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class Rate {
    private Date rateFromDate;
    private Date rateToDate;
    private Set<Price> prices;
}
