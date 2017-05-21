package projectpackage.repository.roomsdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

@Repository
public class RoomTypeDAOImpl extends AbstractDAO implements RoomTypeDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public RoomType getRoomType(Integer id) {
        if (null==id) return null;
        try {
            return (RoomType) manager.createReactEAV(RoomType.class).fetchChildEntityCollection(Rate.class).fetchChildEntityCollectionForInnerObject(Price.class).closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        try {
            return manager.createReactEAV(RoomType.class).fetchChildEntityCollection(Rate.class).fetchChildEntityCollectionForInnerObject(Price.class).closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public int insertRoomType(RoomType roomType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 5, null, null);

            jdbcTemplate.update(insertAttribute, 28, objectId, roomType.getRoomTypeTitle(), null);
            jdbcTemplate.update(insertAttribute, 29, objectId, roomType.getContent(), null);
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
        return objectId;
    }

    @Override
    public void updateRoomType(RoomType newRoomType, RoomType oldRoomType) throws TransactionException {
        try {
            if (!oldRoomType.getRoomTypeTitle().equals(newRoomType.getRoomTypeTitle())) {
                jdbcTemplate.update(updateAttribute, newRoomType.getRoomTypeTitle(), null, newRoomType.getObjectId(), 28);
            }
            if (!oldRoomType.getContent().equals(newRoomType.getContent())) {
                jdbcTemplate.update(updateAttribute, newRoomType.getContent(), null, newRoomType.getObjectId(), 29);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
    }

    @Override
    public int deleteRoomType(int id) {
        return deleteSingleEntityById(id);
    }
}
