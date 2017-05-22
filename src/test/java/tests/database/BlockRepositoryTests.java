package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.blockservice.BlockService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
public class BlockRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(BlockRepositoryTests.class);

    @Autowired
    BlockService blockService;

    @Test
    @Rollback(true)
    public void getAllBlocks() {
        List<Block> blocks = blockService.getAllBlocks();
        for (Block block : blocks) {
            LOGGER.info(block);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleBlockById(){
        Block block = blockService.getSingleBlockById(227);// check id
        LOGGER.info(block);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteBlock(){
        int blockId = 2008;
        IUDAnswer iudAnswer = blockService.deleteBlock(blockId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete block result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createBlock(){
        Block block = new Block();
        block.setBlockStartDate(new Date());
        block.setBlockFinishDate(new Date());
        block.setReason("Reason");
        Room room = new Room();
        room.setObjectId(127);
        room.setNumberOfResidents(1);
        room.setRoomNumber(101);
        block.setRoom(room);
        IUDAnswer iudAnswer = blockService.insertBlock(block);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Insert block result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateBlock(){
        Block block = new Block();
        block.setBlockStartDate(new Date());
        block.setBlockFinishDate(new Date());
        block.setReason("Updated Reason");
        Room room = new Room();
        room.setObjectId(128);
        room.setNumberOfResidents(2);
        room.setRoomNumber(102);
        block.setRoom(room);
        IUDAnswer iudAnswer = blockService.updateBlock(2007, block);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update block result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
