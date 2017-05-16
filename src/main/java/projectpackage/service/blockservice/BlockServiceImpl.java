package projectpackage.service.blockservice;

import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class BlockServiceImpl implements BlockService{
    @Override
    public List<Block> getAllBlocks(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Block> getAllBlocks() {
        return null;
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
        return null;
    }

    @Override
    public boolean deleteBlock(int id) {
        return false;
    }

    @Override
    public boolean insertBlock(Block block) {
        return false;
    }

    @Override
    public boolean updateBlock(Block newBlock) {
        return false;
    }
}
