package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Category;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface CategoryDAO {
    public Category getCategory(Integer id);
    public List<Category> getAllCategories();
    public Integer insertCategory(Category category);
    public Integer updateCategory(Category newCategory, Category oldCategory);
    public void deleteCategory(Integer id);
}
