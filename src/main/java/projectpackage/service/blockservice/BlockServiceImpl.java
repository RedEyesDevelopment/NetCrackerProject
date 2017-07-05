package projectpackage.service.blockservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.service.support.ServiceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
@Service
public class BlockServiceImpl implements BlockService{
    private static final Logger LOGGER = Logger.getLogger(BlockServiceImpl.class);

    @Autowired
    BlockDAO blockDAO;

    @Autowired
    ServiceUtils serviceUtils;

    @Transactional(readOnly = true)
    @Override
    public List<Block> getAllBlocks() {
        List<Block> blocks = blockDAO.getAllBlocks();
        if (blocks == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return blocks;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public Block getSingleBlockById(Integer id) {
        Block block = blockDAO.getBlock(id);
        if (block == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return block;
    }

    @Transactional
    @Override
    public IUDAnswer deleteBlock(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        blockDAO.deleteBlock(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertBlock(Block block) {
        if (block == null) {
            return null;
        }
        boolean isValidDates = serviceUtils.checkDates(block.getBlockStartDate(), block.getBlockFinishDate());
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }
        Integer blockId = blockDAO.insertBlock(block);

        return new IUDAnswer(blockId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateBlock(Integer id, Block newBlock) {
        if (newBlock == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        boolean isValidDates = serviceUtils.checkDates(newBlock.getBlockStartDate(), newBlock.getBlockFinishDate());
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }

        newBlock.setObjectId(id);
        Block oldBlock = blockDAO.getBlock(id);
        blockDAO.updateBlock(newBlock, oldBlock);

        return new IUDAnswer(id, true);
    }
}
