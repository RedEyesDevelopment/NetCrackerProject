package projectpackage.service.blockservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;

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

    // todo подумать можно ли использовать готовую реализацию из OrderService
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
            return new IUDAnswer(false, e.printReferencesEntities());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer insertBlock(Block block) {
        try {
            int blockId = blockDAO.insertBlock(block);
            LOGGER.info("Get from DB blockId = " + blockId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
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
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }
}
