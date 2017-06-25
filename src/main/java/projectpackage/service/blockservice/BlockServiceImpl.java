package projectpackage.service.blockservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class BlockServiceImpl implements BlockService{
    private static final Logger LOGGER = Logger.getLogger(BlockServiceImpl.class);

    @Autowired
    BlockDAO blockDAO;

    @Override
    public List<Block> getAllBlocks(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Block> getAllBlocks() {
        List<Block> blocks = blockDAO.getAllBlocks();
        if (blocks == null) LOGGER.info("Returned NULL!!!");
        return blocks;
    }

    @Override
    public List<Block> getBlocksByRoom(Room room) {
        List<Block> answer = new ArrayList<>();
        Integer roomNumber = room.getRoomNumber();
        List<Block> allBlocks = getAllBlocks();
        for (Block block : allBlocks) {
            if (block.getRoom().getRoomNumber().equals(roomNumber)) {
                answer.add(block);
            }
        }
        return answer;
    }

    // todo нужно ли это?
    @Override
    public List<Block> getBlocksInRange(Date startDate, Date finishDate) {
        return null;
    }

    @Override
    public List<Block> getCurrentBlocks() {
        List<Block> answer = new ArrayList<>();
        List<Block> allBlocks = getAllBlocks();
        Date date = new Date();
        for (Block block : allBlocks) {
            if (block.getBlockStartDate().getTime() < date.getTime()
                    && block.getBlockStartDate().getTime() > date.getTime()) {
                answer.add(block);
            }
        }
        return answer;
    }

    @Override
    public List<Block> getPreviousBlocks() {
        List<Block> answer = new ArrayList<>();
        List<Block> allBlocks = getAllBlocks();
        Date date = new Date();
        for (Block block : allBlocks) {
            if (block.getBlockFinishDate().getTime() < date.getTime()) {
                answer.add(block);
            }
        }
        return answer;
    }

    @Override
    public List<Block> getFutureBlocks() {
        List<Block> answer = new ArrayList<>();
        List<Block> allBlocks = getAllBlocks();
        Date date = new Date();
        for (Block block : allBlocks) {
            if (block.getBlockStartDate().getTime() > date.getTime()) {
                answer.add(block);
            }
        }
        return answer;
    }

    @Override
    public Block getSingleBlockById(Integer id) {
        Block block = blockDAO.getBlock(id);
        if (block == null) LOGGER.info("Returned NULL!!!");
        return block;
    }

    @Override
    public IUDAnswer deleteBlock(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            blockDAO.deleteBlock(id);
        } catch (ReferenceBreakException e) {
            return blockDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return blockDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return blockDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return blockDAO.rollback(id, NULL_ID, e);
        }
        blockDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertBlock(Block block) {
        if (block == null) return null;
        Integer blockId = null;
        try {
            blockId = blockDAO.insertBlock(block);
        } catch (IllegalArgumentException e) {
            return blockDAO.rollback(WRONG_FIELD, e);
        }
        blockDAO.commit();
        return new IUDAnswer(blockId,true);
    }

    @Override
    public IUDAnswer updateBlock(Integer id, Block newBlock) {
        if (newBlock == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newBlock.setObjectId(id);
            Block oldBlock = blockDAO.getBlock(id);
            blockDAO.updateBlock(newBlock, oldBlock);
        } catch (IllegalArgumentException e) {
            return blockDAO.rollback(WRONG_FIELD, e);
        }
        blockDAO.commit();
        return new IUDAnswer(id, true);
    }
}
