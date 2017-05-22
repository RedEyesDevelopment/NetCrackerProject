package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.blocks.Block;
import projectpackage.model.orders.Category;
import projectpackage.model.rooms.Room;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.orderservice.CategoryService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 22.05.2017.
 */
@Log4j
public class CategoryRepositoryTests extends AbstractDatabaseTest {
    private static final Logger LOGGER = Logger.getLogger(CategoryRepositoryTests.class);

    @Autowired
    CategoryService categoryService;

    @Test
    @Rollback(true)
    public void getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            LOGGER.info(category);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleCategoryById(){
        Category category = categoryService.getSingleCategoryById(2007);// check id
        LOGGER.info(category);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteCategory(){
        int categoryId = 2008;
        IUDAnswer iudAnswer = categoryService.deleteCategory(categoryId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete category result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createCategory(){
        Category category = new Category();
        category.setCategoryPrice(5345534L);
        category.setCategoryTitle("Title");
        IUDAnswer iudAnswer = categoryService.insertCategory(category);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Insert category result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateCategory(){
        Category category = new Category();
        category.setCategoryPrice(4L);
        category.setCategoryTitle("new Title");
        IUDAnswer iudAnswer = categoryService.updateCategory(53, category);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update category result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
