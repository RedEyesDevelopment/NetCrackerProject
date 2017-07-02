package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.orders.Category;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.orderservice.CategoryService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    @Rollback
    public void crudCategoryTest() {
        Category insertCategory = new Category();
        insertCategory.setCategoryPrice(5345534L);
        insertCategory.setCategoryTitle("Title");
        IUDAnswer insertAnswer = categoryService.insertCategory(insertCategory);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Insert category result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int categoryId = insertAnswer.getObjectId();

        Category insertedCategory = categoryService.getSingleCategoryById(categoryId);
        insertCategory.setObjectId(categoryId);
        assertEquals(insertCategory, insertedCategory);

        Category updateCategory = new Category();
        updateCategory.setCategoryPrice(4L);
        updateCategory.setCategoryTitle("new Title");
        IUDAnswer updateAnswer = categoryService.updateCategory(categoryId, updateCategory);
        assertTrue(updateAnswer.isSuccessful());
        LOGGER.info("Update category result = " + updateAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Category updatedCategory = categoryService.getSingleCategoryById(categoryId);
        assertEquals(updateCategory, updatedCategory);

        IUDAnswer iudAnswer = categoryService.deleteCategory(categoryId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete category result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
        Category deletedCategory = categoryService.getSingleCategoryById(categoryId);
        assertNull(deletedCategory);
    }

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
        Category category = categoryService.getSingleCategoryById(450);
        LOGGER.info(category);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteCategory(){
        int categoryId = 2049;
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
        IUDAnswer iudAnswer = categoryService.updateCategory(2049, category);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update category result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
