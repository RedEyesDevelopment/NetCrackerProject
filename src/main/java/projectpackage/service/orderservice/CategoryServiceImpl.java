package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.orders.Category;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;
import projectpackage.repository.ordersdao.CategoryDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

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
    public Category getSingleCategoryById(Integer id) {
        Category category = categoryDAO.getCategory(id);
        if (null == category) LOGGER.info("Returned NULL!!!");
        return category;
    }

    @Override
    public IUDAnswer deleteCategory(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            categoryDAO.deleteCategory(id);
        } catch (ReferenceBreakException e) {
            return categoryDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return categoryDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return categoryDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return categoryDAO.rollback(id, NULL_ID, e);
        }
        categoryDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertCategory(Category category) {
        Integer categoryId = null;
        try {
            categoryId = categoryDAO.insertCategory(category);
        } catch (IllegalArgumentException e) {
            return categoryDAO.rollback(WRONG_FIELD, e);
        }
        categoryDAO.commit();
        return new IUDAnswer(categoryId,true);
    }

    @Override
    public IUDAnswer updateCategory(Integer id, Category newCategory) {
        if (newCategory == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newCategory.setObjectId(id);
            Category oldCategory = categoryDAO.getCategory(id);
            categoryDAO.updateCategory(newCategory, oldCategory);
        } catch (IllegalArgumentException e) {
            return categoryDAO.rollback(WRONG_FIELD, e);
        }
        categoryDAO.commit();
        return new IUDAnswer(id,true);
    }
}
