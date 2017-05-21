package projectpackage.service.blockservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
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
    public boolean deleteBlock(int id) {
        int count = blockDAO.deleteBlock(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertBlock(Block block) {
        try {
            int blockId = blockDAO.insertBlock(block);
            LOGGER.info("Get from DB blockId = " + blockId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateBlock(int id, Block newBlock) {
        try {
            newBlock.setObjectId(id);
            Block oldBlock = blockDAO.getBlock(id);
            LOGGER.info("Problem with old BLOCK!! " + oldBlock.getBlockStartDate() + "  " + oldBlock.getBlockFinishDate());
            blockDAO.updateBlock(newBlock, oldBlock);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
