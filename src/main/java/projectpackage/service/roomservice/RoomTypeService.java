package projectpackage.service.roomservice;

import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.MessageBook;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomTypeService extends MessageBook{
    public List<RoomType> getRoomTypes(Date date);
    public List<RoomType> getRoomTypes(Date date, long maxRate);
    public List<RoomType> getRoomTypes(Date date, int numberOfPeople);
    //возвращает типы номеров которые стоят не более maxRate и numberOfPeople человек на указанную date
    public List<RoomType> getRoomTypes(Date date, long maxRate, int numberOfPeople);

    public List<OrderDTO> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId);

    public List<RoomType> getAllRoomTypes();
    public List<RoomType> getAllRoomTypes(String orderingParameter, boolean ascend);
    public RoomType getSingleRoomTypeById(Integer id);
    public IUDAnswer deleteRoomType(Integer id);
    public IUDAnswer insertRoomType(RoomType roomType);
    public IUDAnswer updateRoomType(Integer id, RoomType newRoomType);
}
