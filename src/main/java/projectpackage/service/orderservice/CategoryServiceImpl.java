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
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
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
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
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
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id,false, WRONG_FIELD, e.getMessage());
        }
        categoryDAO.commit();
        return new IUDAnswer(id,true);
    }
}
