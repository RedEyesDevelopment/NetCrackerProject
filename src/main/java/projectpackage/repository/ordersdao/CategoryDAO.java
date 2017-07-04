package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Category;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface CategoryDAO extends Commitable, Rollbackable{
    public Category getCategory(Integer id);
    public List<Category> getAllCategories();
    public List<Category> getSimpleCategoryList();
    public Integer insertCategory(Category category);
    public Integer updateCategory(Category newCategory, Category oldCategory);
    public void deleteCategory(Integer id);
}
