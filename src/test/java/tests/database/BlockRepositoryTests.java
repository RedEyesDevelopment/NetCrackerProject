package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.blocks.Block;
import projectpackage.service.blockservice.BlockService;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
/**
 * Created by Arizel on 16.05.2017.
 */
public class BlockRepositoryTests extends AbstractDatabaseTest{
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    BlockService blockService;

    @Test
    @Rollback(true)
    public void getAllBlocks() {


    }

    @Test
    @Rollback(true)
    public void getSingleBlockById(){

    }


    @Test
    @Rollback(true)
    public void deleteBlock(){

    }

    @Test
    @Rollback(true)
    public void createBlock(){

    }

    @Test
    @Rollback(true)
    public void updateBlock(){

    }
}
