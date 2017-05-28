package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projectpackage.model.orders.Category;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
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
    public IUDAnswer deleteCategory(int id) {
        try {
            categoryDAO.deleteCategory(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(false, e.printReferencesEntities());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer insertCategory(Category category) {
        try {
            int categoryId = categoryDAO.insertCategory(category);
            LOGGER.info("Get from DB categoryId = " + categoryId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer updateCategory(int id, Category newCategory) {
        try {
            newCategory.setObjectId(id);
            Category oldCategory = categoryDAO.getCategory(id);
            categoryDAO.updateCategory(newCategory, oldCategory);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }
}
