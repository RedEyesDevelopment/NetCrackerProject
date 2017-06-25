package projectpackage.service.orderservice;

import projectpackage.model.orders.Category;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface CategoryService extends MessageBook{
    public List<Category> getAllCategories();
    public Category getSingleCategoryById(Integer id);
    public IUDAnswer deleteCategory(Integer id);
    public IUDAnswer insertCategory(Category category);
    public IUDAnswer updateCategory(Integer id, Category newCategory);
}
