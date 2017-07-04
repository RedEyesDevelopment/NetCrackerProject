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
import projectpackage.service.support.ServiceUtils;

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

    @Autowired
    ServiceUtils serviceUtils;

    @Override
    public List<Block> getAllBlocks() {
        List<Block> blocks = blockDAO.getAllBlocks();
        if (blocks == null) {
            LOGGER.info("Returned NULL!!!");
        }
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
        if (block == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return block;
    }

    @Override
    public IUDAnswer deleteBlock(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            blockDAO.deleteBlock(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
        }
        blockDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertBlock(Block block) {
        if (block == null) {
            return null;
        }
        boolean isValidDates = serviceUtils.checkDates(block.getBlockStartDate(), block.getBlockFinishDate());
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }
        Integer blockId = null;
        try {
            blockId = blockDAO.insertBlock(block);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        blockDAO.commit();
        return new IUDAnswer(blockId,true);
    }

    @Override
    public IUDAnswer updateBlock(Integer id, Block newBlock) {
        if (newBlock == null) {
            return null;
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        boolean isValidDates = serviceUtils.checkDates(newBlock.getBlockStartDate(), newBlock.getBlockFinishDate());
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }
        try {
            newBlock.setObjectId(id);
            Block oldBlock = blockDAO.getBlock(id);
            blockDAO.updateBlock(newBlock, oldBlock);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
        }
        blockDAO.commit();
        return new IUDAnswer(id, true);
    }
}
