package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.rates.Rate;

import java.util.Set;

@Data
public class RoomType {
    private String roomTypeTitle;
    private String content;
    private Set<Rate> rates;
}
