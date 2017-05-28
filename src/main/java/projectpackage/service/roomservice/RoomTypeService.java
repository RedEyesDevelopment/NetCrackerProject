package projectpackage.service.roomservice;

import projectpackage.model.rooms.RoomType;
import projectpackage.dto.IUDAnswer;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomTypeService {
    public List<RoomType> getRoomTypes(Date date);
    public List<RoomType> getRoomTypes(Date date, long maxRate);
    public List<RoomType> getRoomTypes(Date date, int numberOfPeople);
    public List<RoomType> getRoomTypes(Date date, long maxRate, int numberOfPeople);//возвращает типы номеров которые стоят не более maxRate и numberOfPeople человек на указанную date//TODO Denis

    public List<RoomType> getAllRoomTypes();
    public List<RoomType> getAllRoomTypes(String orderingParameter, boolean ascend);
    public RoomType getSingleRoomTypeById(int id);
    public IUDAnswer deleteRoomType(int id);
    public IUDAnswer insertRoomType(RoomType roomType);
    public IUDAnswer updateRoomType(int id, RoomType newRoomType);
}
