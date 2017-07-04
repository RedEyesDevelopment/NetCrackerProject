package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.RoomType;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RoomTypeDAO extends Commitable, Rollbackable {
    public RoomType getRoomType(Integer id);
    public List<RoomType> getAllRoomTypes();
    public List<RoomType> getSimpleRoomTypeList();
    public Set<Integer> getAvailableRoomTypes(int numberOfPeople, Date startDate, Date finishDate);
    public long getCostForLiving(RoomType roomType, int numberOfResidents, Date start, Date finish);
    public Integer insertRoomType(RoomType roomType);
    public Integer updateRoomType(RoomType newRoomType, RoomType oldRoomType);
    public void deleteRoomType(Integer id);
}
