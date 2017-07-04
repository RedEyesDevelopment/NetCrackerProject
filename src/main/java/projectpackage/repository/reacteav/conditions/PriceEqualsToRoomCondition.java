package projectpackage.repository.reacteav.conditions;

import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.AfterQueryConditionExecutor;

import java.util.*;

public class PriceEqualsToRoomCondition implements ReactConditionAfterExecution {
    private List<Object> objects;

    @Override
    public Class getNeededConditionExecutor() {
        return AfterQueryConditionExecutor.class;
    }

    @Override
    public Class getTargetClass() {
        return Room.class;
    }

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
                Rate newRate = rate.clone();
                Set<Price> prices = new HashSet<>();
                for (Price price : rate.getPrices()) {
                    if (price.getNumberOfPeople().equals(roomQuantityNumber)) {
                        prices.add(price);
                    }
                }
                newRate.setPrices(prices);
                rates.add(newRate);
            }
            newRates.put(room.getObjectId(),rates);
        }
        setRates(newRates);
    }

    private void setRates(Map<Integer, Set<Rate>> newRates){
        for (Object obj : objects) {
            Room room = (Room) obj;
            for (Map.Entry<Integer, Set<Rate>> entry:newRates.entrySet()){
                if (entry.getKey().equals(room.getObjectId())){
                    RoomType newRoomType = room.getRoomType().clone();
                    newRoomType.setRates(entry.getValue());
                    room.setRoomType(newRoomType);
                }
            }
        }
    }
}
