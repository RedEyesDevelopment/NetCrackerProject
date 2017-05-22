package projectpackage.service.blockservice;

import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.model.support.IUDAnswer;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockService {
    public List<Block> getBlocksByRoom(Room room);
    public List<Block> getBlocksInRange(Date startDate, Date finishDate);
    public List<Block> getCurrentBlocks();//livingStartDate < SYSDATE < livingFinishDate ясно?
    public List<Block> getPreviousBlocks();//livingFinishDate < SYSDATE
    public List<Block> getFutureBlocks();//SYSDATE < livingStartDate

    public List<Block> getAllBlocks(String orderingParameter, boolean ascend);
    public List<Block> getAllBlocks();
    public Block getSingleBlockById(int id);
    public IUDAnswer deleteBlock(int id);
    public IUDAnswer insertBlock(Block block);
    public IUDAnswer updateBlock(int id, Block newBlock);
}
