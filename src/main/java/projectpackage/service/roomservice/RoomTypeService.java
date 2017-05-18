package projectpackage.service.roomservice;

import projectpackage.model.rooms.RoomType;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomTypeService {
    public List<RoomType> getRoomTypes(Date date);//TODO Denis
    public List<RoomType> getRoomTypes(Date date, long maxRate);//TODO Denis
    public List<RoomType> getRoomTypes(Date date, int numberOfPeople);//TODO Denis
    public List<RoomType> getRoomTypes(Date date, long maxRate, int numberOfPeople);//возвращает типы номеров которые стоят не более maxRate и numberOfPeople человек на указанную date//TODO Denis

    public List<RoomType> getAllRoomTypes();//TODO Pacanu
    public List<RoomType> getAllRoomTypes(String orderingParameter, boolean ascend);//TODO Pacanu
    public RoomType getSingleRoomTypeById(int id);//TODO Pacanu
    public boolean deleteRoomType(int id);
    public boolean insertRoomType(RoomType roomType);
    public boolean updateRoomType(int id, RoomType newRoomType);
}
