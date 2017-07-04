package projectpackage.repository.ordersdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Category;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

@Repository
public class CategoryDAOImpl extends AbstractDAO implements CategoryDAO {
    private static final Logger LOGGER = Logger.getLogger(CategoryDAOImpl.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Category getCategory(Integer id) {
        if (null == id) {
            return null;
        }

        return (Category) manager.createReactEAV(Category.class)
                .fetchRootChild(Complimentary.class)
                .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                .closeAllFetches()
                .getSingleEntityWithId(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return manager.createReactEAV(Category.class)
                .fetchRootChild(Complimentary.class)
                .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                .closeAllFetches()
                .getEntityCollection();
    }

    @Override
    public List<Category> getSimpleCategoryList() {
        return manager.createReactEAV(Category.class).getEntityCollection();
    }

    @Override
    public Integer insertCategory(Category category) {
        if (category == null) {
            return null;
        }
        Integer objectId = nextObjectId();

        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 13, null, null);
        insertPrice(category, objectId);
        insertTitle(category, objectId);

        return objectId;
    }

    @Override
    public Integer updateCategory(Category newCategory, Category oldCategory) {
        if (newCategory == null || oldCategory == null) {
            return null;
        }

        updateCategoryTitle(newCategory, oldCategory);
        updateCategoryPrice(newCategory, oldCategory);

        return newCategory.getObjectId();
    }

    @Override
    public void deleteCategory(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Category category = null;
        try {
            category = getCategory(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == category) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertTitle(Category category, Integer objectId) {
        if (category.getCategoryTitle() != null && !category.getCategoryTitle().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 45, objectId, category.getCategoryTitle(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertPrice(Category category, Integer objectId) {
        if (category.getCategoryPrice() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 46, objectId, category.getCategoryPrice(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateCategoryTitle(Category newCategory, Category oldCategory) {
        if (oldCategory.getCategoryTitle() != null && newCategory.getCategoryTitle() != null
                && !newCategory.getCategoryTitle().isEmpty()) {
            if (!oldCategory.getCategoryTitle().equals(newCategory.getCategoryTitle())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newCategory.getCategoryTitle(), null, newCategory.getObjectId(), 45);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateCategoryPrice(Category newCategory, Category oldCategory) {
        if (oldCategory.getCategoryPrice() != null && newCategory.getCategoryPrice() != null) {
            if (!oldCategory.getCategoryPrice().equals(newCategory.getCategoryPrice())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newCategory.getCategoryPrice(), null, newCategory.getObjectId(), 46);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
