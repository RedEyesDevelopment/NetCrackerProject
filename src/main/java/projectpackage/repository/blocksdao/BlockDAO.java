package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockDAO {
    public Block getBlock(Integer id);
    public List<Block> getAllBlocks();
    public int insertBlock(Block block) throws TransactionException;
    public void updateBlock(Block newBlock, Block oldBlock) throws TransactionException;
    public int deleteBlock(int id);
}
