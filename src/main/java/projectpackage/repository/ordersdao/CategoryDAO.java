package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Category;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface CategoryDAO {
    public Category getCategory(Integer id);
    public List<Category> getAllCategories();
    public Integer insertCategory(Category category) throws TransactionException;
    public Integer updateCategory(Category newCategory, Category oldCategory) throws TransactionException;
    public void deleteCategory(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
