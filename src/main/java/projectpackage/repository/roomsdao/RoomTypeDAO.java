package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.RoomType;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomTypeDAO {
    public int insertRoomType(RoomType roomType) throws TransactionException;
    public void updateRoomType(RoomType newRoomType, RoomType oldRoomType) throws TransactionException;
    public int deleteRoomType(int id);
}