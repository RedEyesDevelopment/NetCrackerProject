package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.RoomType;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;

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
    public int insertRoomType(RoomType roomType) throws TransactionException;
    public void updateRoomType(RoomType newRoomType, RoomType oldRoomType) throws TransactionException;
    public void deleteRoomType(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
