package projectpackage.repository.reacteav.conditions;

import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;

import java.util.*;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class PriceEqualsToRoomCondition implements ReactCondition {
    private List<Object> objects;

    @Override
    public void loadDataToParse(List<Object> data) {
        this.objects = data;
    }

    public void execute() {
        Map<Integer, Set<Rate>> newRates = new HashMap<>();
        for (Object obj : objects) {
            Room room = (Room) obj;
            int roomQuantityNumber = room.getNumberOfResidents();
            RoomType roomType = room.getRoomType();
            Set<Rate> rates = new HashSet<>();
            for (Rate rate : roomType.getRates()) {
                Rate newRate = (Rate) rate.clone();
                Set<Price> prices = new HashSet<>();
                for (Price price : rate.getPrices()) {
                    if (price.getNumberOfPeople().equals(roomQuantityNumber)) {
                        prices.add(price);
                    }
                }
                newRate.setPrices(prices);
                rates.add(newRate);
                System.out.println();
            }
            newRates.put(room.getObjectId(),rates);
        }
        setRates(newRates);
    }

    public void setRates(Map<Integer, Set<Rate>> newRates){
        for (Object obj : objects) {
            Room room = (Room) obj;
            for (Map.Entry<Integer, Set<Rate>> entry:newRates.entrySet()){
                if (entry.getKey().equals(room.getObjectId())){
                    RoomType newRoomType = (RoomType) room.getRoomType().clone();
                    newRoomType.setRates(entry.getValue());
                    room.setRoomType(newRoomType);
                }
            }
        }
    }
}
