package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface BlockDAO {
    public Block getBlock(Integer id);
    public List<Block> getAllBlocks();
    public Integer insertBlock(Block block) throws TransactionException;
    public Integer updateBlock(Block newBlock, Block oldBlock) throws TransactionException;
    public void deleteBlock(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
