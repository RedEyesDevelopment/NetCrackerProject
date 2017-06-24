package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.RoomType;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomTypeDAO {
    public RoomType getRoomType(Integer id);
    public List<RoomType> getAllRoomTypes();
    public Set<Integer> getAvailableRoomTypes(int numberOfPeople, Date startDate, Date finishDate);
    public long getCostForLiving(RoomType roomType, int numberOfResidents, Date start, Date finish);
    public Integer insertRoomType(RoomType roomType);
    public Integer updateRoomType(RoomType newRoomType, RoomType oldRoomType);
    public void deleteRoomType(Integer id);
}
