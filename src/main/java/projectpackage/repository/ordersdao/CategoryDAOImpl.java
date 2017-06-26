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
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

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
    public int insertCategory(Category category) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 13, null, null);

            jdbcTemplate.update(insertAttribute, 45, objectId, category.getCategoryTitle(), null);
            jdbcTemplate.update(insertAttribute, 46, objectId, category.getCategoryPrice(), null);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateCategory(Category newCategory, Category oldCategory) throws TransactionException {
        try {
            if (!oldCategory.getCategoryTitle().equals(newCategory.getCategoryTitle())) {
                jdbcTemplate.update(updateAttribute, newCategory.getCategoryTitle(), null, newCategory.getObjectId(), 45);
            }
            if (!oldCategory.getCategoryPrice().equals(newCategory.getCategoryPrice())) {
                jdbcTemplate.update(updateAttribute, newCategory.getCategoryPrice(), null, newCategory.getObjectId(), 46);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
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
}
