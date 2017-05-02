package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.Model;
import projectpackage.service.ModelService;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional
public class RepositoryTests extends AbstractDatabaseTest {

    @Autowired
    ModelService modelService;

    @Test
    @Rollback(true)
    public void getModel() {

        System.out.println(modelService.toString());
        System.out.println("****************************************************************");
        Model model = modelService.getModel(1);
        System.out.println(model.toString());
        System.out.println("****************************************************************");
    }


}