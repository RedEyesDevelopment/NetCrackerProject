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
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

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
    public void deleteCategory(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }
}
