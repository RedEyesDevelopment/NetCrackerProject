package projectpackage.repository.ordersdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Category;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.*;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Repository
public class CategoryDAOImpl extends AbstractDAO implements CategoryDAO {
    private static final Logger LOGGER = Logger.getLogger(CategoryDAOImpl.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Category getCategory(Integer id) {
        if (null == id) return null;
        try {
            return (Category) manager.createReactEAV(Category.class)
                    .fetchRootChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                    .closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return manager.createReactEAV(Category.class)
                    .fetchRootChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                    .closeAllFetches()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public Integer insertCategory(Category category) throws TransactionException {
        if (category == null) return null;
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, null, 13, null, null);
            insertPrice(category, objectId);
            insertTitle(category, objectId);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public Integer updateCategory(Category newCategory, Category oldCategory) throws TransactionException {
        if (newCategory == null || oldCategory == null) return null;
        try {
            updateCategoryTitle(newCategory, oldCategory);
            updateCategoryPrice(newCategory, oldCategory);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newCategory.getObjectId();
    }

    @Override
    public void deleteCategory(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Category category = null;
        try {
            category = getCategory(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == category) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }

    private void insertTitle(Category category, Integer objectId) {
        if (category.getCategoryTitle() != null && !category.getCategoryTitle().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 45, objectId, category.getCategoryTitle(), null);
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void insertPrice(Category category, Integer objectId) {
        if (category.getCategoryPrice() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 46, objectId, category.getCategoryPrice(), null);
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateCategoryTitle(Category newCategory, Category oldCategory) {
        if (oldCategory.getCategoryTitle() != null && newCategory.getCategoryTitle() != null
                && !newCategory.getCategoryTitle().isEmpty()) {
            if (!oldCategory.getCategoryTitle().equals(newCategory.getCategoryTitle())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newCategory.getCategoryTitle(), null, newCategory.getObjectId(), 45);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateCategoryPrice(Category newCategory, Category oldCategory) {
        if (oldCategory.getCategoryPrice() != null && newCategory.getCategoryPrice() != null) {
            if (!oldCategory.getCategoryPrice().equals(newCategory.getCategoryPrice())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newCategory.getCategoryPrice(), null, newCategory.getObjectId(), 46);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }
}
