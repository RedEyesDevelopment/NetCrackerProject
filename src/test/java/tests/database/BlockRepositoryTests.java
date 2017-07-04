package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.blockservice.BlockService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void crudBlockTest() {
        Block block = new Block();
        //block.setBlockStartDate(new Date(16000L));
        block.setBlockStartDate(new Date());
        block.setBlockFinishDate(new Date());
        //block.setBlockFinishDate(new Date(16000L));
        block.setReason("Reason");
        Room room = new Room();
        room.setObjectId(127);
        room.setNumberOfResidents(1);
        room.setRoomNumber(101);
        block.setRoom(room);
        IUDAnswer iudAnswerInsert = blockService.insertBlock(block);
        assertTrue(iudAnswerInsert.isSuccessful());
        LOGGER.info("Insert block result = " + iudAnswerInsert.isSuccessful());
        LOGGER.info(SEPARATOR);

        int blockId = iudAnswerInsert.getObjectId();
        Block insertedBlock = blockService.getSingleBlockById(blockId);

        block.setObjectId(blockId);
        LOGGER.info("TIME EQUALS? = " + block.getBlockStartDate().getTime() + " : " + insertedBlock.getBlockStartDate().getTime() + block.getBlockStartDate().equals(insertedBlock.getBlockStartDate()));
        assertEquals(block, insertedBlock);

        Block newBlock = new Block();
        //newBlock.setBlockStartDate(new Date(17000L));
        newBlock.setBlockStartDate(new Date());
        newBlock.setBlockFinishDate(new Date());
        //newBlock.setBlockFinishDate(new Date(17000L));
        newBlock.setReason("Updated Reason");
        Room newRoom = new Room();
        newRoom.setObjectId(128);
        newRoom.setNumberOfResidents(2);
        newRoom.setRoomNumber(102);
        newBlock.setRoom(newRoom);
        IUDAnswer iudAnswerUpdate = blockService.updateBlock(blockId, newBlock);
        assertTrue(iudAnswerUpdate.isSuccessful());
        LOGGER.info("Update block result = " + iudAnswerUpdate.isSuccessful());
        LOGGER.info(SEPARATOR);

        Block updatedBlock = blockService.getSingleBlockById(blockId);
        assertEquals(newBlock, updatedBlock);

        IUDAnswer iudAnswerDelete = blockService.deleteBlock(blockId);
        assertTrue(iudAnswerDelete.isSuccessful());
        LOGGER.info("Delete block result = " + iudAnswerDelete.isSuccessful());
        LOGGER.info(SEPARATOR);
        Block deletedBlock = blockService.getSingleBlockById(blockId);
        assertNull(deletedBlock);
    }

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
        Block block = blockService.getSingleBlockById(2381);
        LOGGER.info(block);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteBlock(){
        int blockId = 2107;
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
        LOGGER.info("Insert block result = " + iudAnswer.getObjectId());
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
        IUDAnswer iudAnswer = blockService.updateBlock(2107, block);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update block result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
