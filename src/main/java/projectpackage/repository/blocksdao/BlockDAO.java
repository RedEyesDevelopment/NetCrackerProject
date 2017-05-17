package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockDAO {
    public int insertBlock(Block block) throws TransactionException;
    public void updateBlock(Block newBlock, Block oldBlock) throws TransactionException;
    public int deleteBlock(int id);
}