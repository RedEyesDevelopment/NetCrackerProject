package projectpackage.service.blockservice;

import projectpackage.dto.IUDAnswer;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.service.MessageBook;

import java.util.List;

public interface BlockService extends MessageBook{
    public List<Block> getBlocksByRoom(Room room);
    public List<Block> getCurrentBlocks();//livingStartDate < SYSDATE < livingFinishDate ясно?
    public List<Block> getPreviousBlocks();//livingFinishDate < SYSDATE
    public List<Block> getFutureBlocks();//SYSDATE < livingStartDate

    public List<Block> getAllBlocks();
    public Block getSingleBlockById(Integer id);
    public IUDAnswer deleteBlock(Integer id);
    public IUDAnswer insertBlock(Block block);
    public IUDAnswer updateBlock(Integer id, Block newBlock);
}
