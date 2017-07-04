package projectpackage.service.roomservice;

import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.MessageBook;

import java.util.Date;
import java.util.List;

public interface RoomTypeService extends MessageBook{
    List<OrderDTO> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId);
    List<RoomType> getAllRoomTypes();
    List<RoomType> getSimpleRoomTypeList();
    Long getLivingCost(Date startDate, Date finishDate, int numberOfPeople, RoomType roomType);
    RoomType getSingleRoomTypeById(Integer id);
    IUDAnswer deleteRoomType(Integer id);
    IUDAnswer insertRoomType(RoomType roomType);
    IUDAnswer updateRoomType(Integer id, RoomType newRoomType);
}
