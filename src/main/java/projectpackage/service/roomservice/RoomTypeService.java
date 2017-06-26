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
    public List<OrderDTO> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId);
    public List<RoomType> getAllRoomTypes();
    public List<RoomType> getSimpleRoomTypeList();
    public RoomType getSingleRoomTypeById(Integer id);
    public IUDAnswer deleteRoomType(Integer id);
    public IUDAnswer insertRoomType(RoomType roomType);
    public IUDAnswer updateRoomType(Integer id, RoomType newRoomType);
}
