package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockDAO {
    public Block getBlock(Integer id);
    public List<Block> getAllBlocks();
    public Integer insertBlock(Block block);
    public Integer updateBlock(Block newBlock, Block oldBlock);
    public void deleteBlock(Integer id);
}
