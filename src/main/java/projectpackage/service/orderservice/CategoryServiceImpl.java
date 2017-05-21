package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.orders.Category;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;
import projectpackage.repository.ordersdao.CategoryDAO;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Repository
@Log4j
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class);

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ComplimentaryDAO complimentaryDAO;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        if (null == categories) LOGGER.info("Returned NULL!!!");
        return categories;
    }

    @Override
    public List<Category> getAllCategories(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Category getSingleCategoryById(int id) {
        Category category = categoryDAO.getCategory(id);
        if (null == category) LOGGER.info("Returned NULL!!!");
        return category;
    }

    @Override
    public boolean deleteCategory(int id) {
        Category category = categoryDAO.getCategory(id);
        for (Complimentary complimentary : category.getComplimentaries()) {
            complimentaryDAO.deleteComplimentary(complimentary.getObjectId());
        }
        int count = categoryDAO.deleteCategory(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertCategory(Category category) {
        try {
            int categoryId = categoryDAO.insertCategory(category);
            LOGGER.info("Get from DB categoryId = " + categoryId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCategory(int id, Category newCategory) {
        try {
            newCategory.setObjectId(id);
            Category oldCategory = categoryDAO.getCategory(id);
            categoryDAO.updateCategory(newCategory, oldCategory);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
