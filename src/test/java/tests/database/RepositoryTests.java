package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional
public class RepositoryTests extends AbstractDatabaseTest {


    @Test
    @Rollback(true)
    public void getModel() {

    }


}