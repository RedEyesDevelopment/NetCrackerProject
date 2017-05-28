package projectpackage.service.blockservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;

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
        return null;
    }

    @Override
    public List<Block> getBlocksInRange(Date startDate, Date finishDate) {
        return null;
    }

    @Override
    public List<Block> getCurrentBlocks() {
        return null;
    }

    @Override
    public List<Block> getPreviousBlocks() {
        return null;
    }

    @Override
    public List<Block> getFutureBlocks() {
        return null;
    }

    @Override
    public Block getSingleBlockById(int id) {
        Block block = blockDAO.getBlock(id);
        if (block == null) LOGGER.info("Returned NULL!!!");
        return block;
    }

    @Override
    public IUDAnswer deleteBlock(int id) {
        try {
            blockDAO.deleteBlock(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertBlock(Block block) {
        Integer blockId = null;
        try {
            blockId = blockDAO.insertBlock(block);
            LOGGER.info("Get from DB blockId = " + blockId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(blockId, false, "transactionInterrupt");
        }
        return new IUDAnswer(blockId,true);
    }

    @Override
    public IUDAnswer updateBlock(int id, Block newBlock) {
        try {
            newBlock.setObjectId(id);
            Block oldBlock = blockDAO.getBlock(id);
            LOGGER.info("Problem with old BLOCK!! " + oldBlock.getBlockStartDate() + "  " + oldBlock.getBlockFinishDate());
            blockDAO.updateBlock(newBlock, oldBlock);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id, false, "transactionInterrupt");
        }
        return new IUDAnswer(id, true);
    }
}
