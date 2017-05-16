package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;
import projectpackage.repository.reacdao.exceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockDAO {
    public void insertBlock(Block block) throws TransactionException;
    public void updateBlock(Block newBlock, Block oldBlock) throws TransactionException;
}
