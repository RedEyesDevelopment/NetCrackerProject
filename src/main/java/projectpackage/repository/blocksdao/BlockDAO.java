package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface BlockDAO extends Commitable, Rollbackable{
    public Block getBlock(Integer id);
    public List<Block> getAllBlocks();
    public Integer insertBlock(Block block);
    public Integer updateBlock(Block newBlock, Block oldBlock);
    public void deleteBlock(Integer id);
}
