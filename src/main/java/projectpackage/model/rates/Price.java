package projectpackage.model.rates;

import lombok.Data;

@Data
public class Price {
    private Integer numberOfPeople;
    private Long price;
    private Rate rate;
}
