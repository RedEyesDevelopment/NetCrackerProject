package projectpackage.repository.blocksdao;

import projectpackage.model.blocks.Block;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class BlockDAOImpl extends AbstractDAO implements BlockDAO{
    @Override
    public int insertBlock(Block block) throws TransactionException {
        return 0;
    }

    @Override
    public void updateBlock(Block newBlock, Block oldBlock) throws TransactionException {

    }

    @Override
    public int deleteBlock(int id) {
        return deleteSingleEntityById(id);
    }
}