package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.blocks.Block;
import projectpackage.model.rates.Price;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.blockservice.BlockService;

import java.util.Date;

import static org.junit.Assert.assertTrue;
/**
 * Created by Arizel on 16.05.2017.
 */
public class BlockRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(BlockRepositoryTests.class);

    @Autowired
    BlockService blockService;

    @Test
    @Rollback(true)
    public void getAllBlocks() {

    }

    @Test
    @Rollback(true)
    public void getSingleBlockById(){

    }


    @Test
    @Rollback(true)
    public void deleteBlock(){
        int blockId = 2008;
        boolean result = blockService.deleteBlock(blockId);
        assertTrue(result);
        LOGGER.info("Delete block result = " + result);
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
        boolean result = blockService.insertBlock(block);
        assertTrue(result);
        LOGGER.info("Insert block result = " + result);
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
        boolean result = blockService.updateBlock(2007, block);
        assertTrue(result);
        LOGGER.info("Update block result = " + result);
        LOGGER.info(SEPARATOR);
    }
}
