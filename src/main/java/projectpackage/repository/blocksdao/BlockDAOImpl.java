package projectpackage.repository.blocksdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.blocks.Block;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class BlockDAOImpl extends AbstractDAO implements BlockDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insertBlock(Block block) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, null, 8, null, null);

            jdbcTemplate.update(insertAttributes, 35, objectId, null, block.getBlockStartDate());
            jdbcTemplate.update(insertAttributes, 36, objectId, null, block.getBlockFinishDate());
            jdbcTemplate.update(insertAttributes, 37, objectId, block.getReason(), null);

            jdbcTemplate.update(insertObjReference, 34, objectId, block.getRoom().getObjectId());
        } catch (NullPointerException e) {
            throw new TransactionException(block);
        }
        return objectId;
    }

    @Override
    public void updateBlock(Block newBlock, Block oldBlock) throws TransactionException {
        try {
            if (oldBlock.getBlockStartDate().getTime() != newBlock.getBlockStartDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null, newBlock.getBlockStartDate(), newBlock.getObjectId(), 35);
            }
            if (oldBlock.getBlockFinishDate().getTime() != newBlock.getBlockFinishDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null, newBlock.getBlockFinishDate(), newBlock.getObjectId(), 36);
            }
            if (!oldBlock.getReason().equals(newBlock.getReason())) {
                jdbcTemplate.update(updateAttributes, newBlock.getReason(), null, newBlock.getObjectId(), 37);
            }
            if (oldBlock.getRoom().getObjectId() != newBlock.getRoom().getObjectId()) {
                jdbcTemplate.update(updateReference, newBlock.getRoom().getObjectId(), newBlock.getObjectId(), 34);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newBlock);
        }
    }

    @Override
    public int deleteBlock(int id) {
        return deleteSingleEntityById(id);
    }
}
