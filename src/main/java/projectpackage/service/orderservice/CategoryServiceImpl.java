package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.orders.Category;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;
import projectpackage.repository.ordersdao.CategoryDAO;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Service
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
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertCategory(Category category) {
        Integer categoryId = null;
        try {
            categoryId = categoryDAO.insertCategory(category);
            LOGGER.info("Get from DB categoryId = " + categoryId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(categoryId,false, "transactionInterrupt");
        }
        return new IUDAnswer(categoryId,true);
    }

    @Override
    public IUDAnswer updateCategory(int id, Category newCategory) {
        try {
            newCategory.setObjectId(id);
            Category oldCategory = categoryDAO.getCategory(id);
            categoryDAO.updateCategory(newCategory, oldCategory);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
