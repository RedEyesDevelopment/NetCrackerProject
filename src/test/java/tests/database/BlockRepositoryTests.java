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
        int blockId = 227;
        boolean result = blockService.deleteBlock(blockId);
        assertTrue(result);
        LOGGER.info("Delete phone result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createBlock(){
        Price price = new Price();
        price.setNumberOfPeople(1);
        //price.setObjectId();

        Block block = new Block();
        block.setBlockStartDate(new Date());
        block.setBlockFinishDate(new Date());
        block.setReason("Reason");
        Room room = new Room();
        room.setObjectId(127);
        room.setNumberOfResidents(1);
        room.setRoomNumber(101);
        //room.setRoomType(new RoomType().get);
    }

    @Test
    @Rollback(true)
    public void updateBlock(){

    }
}
