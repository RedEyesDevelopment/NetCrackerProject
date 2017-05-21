package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Category;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface CategoryDAO {
    public Category getCategory(Integer id);
    public List<Category> getAllCategories();
    public int insertCategory(Category category) throws TransactionException;
    public void updateCategory(Category newCategory, Category oldCategory) throws TransactionException;
    public void deleteCategory(int id) throws ReferenceBreakException;
}
