package projectpackage.service.blockservice;

import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockService {
    public List<Block> getBlocksByRoom(Room room);//TODO Denis
    public List<Block> getBlocksInRange(Date startDate, Date finishDate); //TODO Denis
    public List<Block> getCurrentBlocks();//livingStartDate < SYSDATE < livingFinishDate ясно? //TODO Denis
    public List<Block> getPreviousBlocks();//livingFinishDate < SYSDATE //TODO Denis
    public List<Block> getFutureBlocks();//SYSDATE < livingStartDate //TODO Denis

    public List<Block> getAllBlocks(String orderingParameter, boolean ascend);//TODO Pacanu
    public List<Block> getAllBlocks();//TODO Pacanu
    public Block getSingleBlockById(int id);//TODO Pacanu
    public boolean deleteBlock(int id);//TODO Pacanu
    public boolean insertBlock(Block block);//TODO Pacanu
    public boolean updateBlock(Block newBlock);//TODO Pacanu
}
