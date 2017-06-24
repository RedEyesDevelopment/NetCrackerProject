package projectpackage.repository.blocksdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.*;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Transactional
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

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = RequiredFieldAbsenceException.class)
    @Override
    public Integer insertBlock(Block block) throws TransactionException {
        if (block == null) return null;

        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, null, 8, null, null);
            insertStartDate(block, objectId);
            insertFinishDate(block, objectId);
            insertReason(objectId, block);
            insertRoom(objectId, block);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public Integer updateBlock(Block newBlock, Block oldBlock) throws TransactionException {
        if (oldBlock == null || newBlock == null) return null;
        try {
            updateStartDate(newBlock, oldBlock);
            updateFinishDate(newBlock, oldBlock);
            updateReason(newBlock, oldBlock);
            updateRoom(newBlock, oldBlock);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newBlock.getObjectId();
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

    private void insertStartDate(Block block, Integer objectId) {
        if (block.getBlockStartDate() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 35, objectId, null, block.getBlockStartDate());
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void insertFinishDate(Block block, Integer objectId) {
        if (block.getBlockFinishDate() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 36, objectId, null, block.getBlockFinishDate());
        } else {

        }
    }

    private void insertReason(Integer objectId, Block block) {
        if (block.getReason() != null && !block.getReason().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 37, objectId, block.getReason(), null);
        } else {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 37, objectId, null, null);
        }
    }

    private void insertRoom(Integer objectId, Block block) {
        if (block.getRoom() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 34, objectId, block.getRoom().getObjectId());
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateStartDate(Block newBlock, Block oldBlock) {
        if (oldBlock.getBlockStartDate() != null && newBlock.getBlockFinishDate() != null) {
            if (oldBlock.getBlockStartDate().getTime() != newBlock.getBlockStartDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newBlock.getBlockStartDate(), newBlock.getObjectId(), 35);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateFinishDate(Block newBlock, Block oldBlock) {
        if (oldBlock.getBlockFinishDate() != null && newBlock.getBlockFinishDate() != null) {
            if (oldBlock.getBlockFinishDate().getTime() != newBlock.getBlockFinishDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newBlock.getBlockFinishDate(), newBlock.getObjectId(), 36);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateReason(Block newBlock, Block oldBlock) {
        if (oldBlock.getReason() != null && newBlock.getReason() != null && !newBlock.getReason().isEmpty()) {
            if (!oldBlock.getReason().equals(newBlock.getReason())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newBlock.getReason(), null, newBlock.getObjectId(), 37);
            }
        } else if (newBlock.getReason() != null && !newBlock.getReason().isEmpty()) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, newBlock.getReason(), null, newBlock.getObjectId(), 37);
        } else if (oldBlock.getReason() != null) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, null, newBlock.getObjectId(), 37);
        }
    }

    private void updateRoom(Block newBlock, Block oldBlock) {
        if (oldBlock.getRoom() != null && newBlock.getRoom() != null) {
            if (oldBlock.getRoom().getObjectId() != newBlock.getRoom().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newBlock.getRoom().getObjectId(), newBlock.getObjectId(), 34);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }
}
