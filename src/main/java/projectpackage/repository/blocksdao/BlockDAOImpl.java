package projectpackage.repository.blocksdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class BlockDAOImpl extends AbstractDAO implements BlockDAO{
    private static final Logger LOGGER = Logger.getLogger(BlockDAOImpl.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Block getBlock(Integer id) {
        if (id == null) return null;
        try {
            return (Block) manager.createReactEAV(Block.class).fetchRootReference(Room.class, "RoomToBlock")
                    .closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Block> getAllBlocks() {
        try {
            return manager.createReactEAV(Block.class).fetchRootReference(Room.class, "RoomToBlock")
                    .closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertBlock(Block block) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 8, null, null);

            jdbcTemplate.update(insertAttribute, 35, objectId, null, block.getBlockStartDate());
            jdbcTemplate.update(insertAttribute, 36, objectId, null, block.getBlockFinishDate());
            jdbcTemplate.update(insertAttribute, 37, objectId, block.getReason(), null);

            jdbcTemplate.update(insertObjReference, 34, objectId, block.getRoom().getObjectId());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateBlock(Block newBlock, Block oldBlock) throws TransactionException {
        try {
            if (oldBlock.getBlockStartDate().getTime() != newBlock.getBlockStartDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null, newBlock.getBlockStartDate(), newBlock.getObjectId(), 35);
            }
            if (oldBlock.getBlockFinishDate().getTime() != newBlock.getBlockFinishDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null, newBlock.getBlockFinishDate(), newBlock.getObjectId(), 36);
            }
            if (!oldBlock.getReason().equals(newBlock.getReason())) {
                jdbcTemplate.update(updateAttribute, newBlock.getReason(), null, newBlock.getObjectId(), 37);
            }
            if (oldBlock.getRoom().getObjectId() != newBlock.getRoom().getObjectId()) {
                jdbcTemplate.update(updateReference, newBlock.getRoom().getObjectId(), newBlock.getObjectId(), 34);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteBlock(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Block block = null;
        try {
            block = getBlock(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == block) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
