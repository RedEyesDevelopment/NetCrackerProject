package projectpackage.service.roomservice;

import projectpackage.model.rooms.RoomType;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomTypeServiceImpl implements RoomTypeService{
    @Override
    public List<RoomType> getRoomTypes(Date date) {
        return null;
    }

    @Override
    public List<RoomType> getRoomTypes(Date date, long maxRate) {
        return null;
    }

    @Override
    public List<RoomType> getRoomTypes(Date date, int numberOfPeople) {
        return null;
    }

    @Override
    public List<RoomType> getRoomTypes(Date date, long maxRate, int numberOfPeople) {
        return null;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return null;
    }

    @Override
    public List<RoomType> getAllRoomTypes(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public RoomType getSingleRoomTypeById(int id) {
        return null;
    }

    @Override
    public boolean deleteRoomType(RoomType roomType) {
        return false;
    }

    @Override
    public boolean insertRoomType(RoomType roomType) {
        return false;
    }

    @Override
    public boolean updateRoomType(RoomType newRoomType) {
        return false;
    }
}
