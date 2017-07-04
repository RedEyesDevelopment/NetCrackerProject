package projectpackage.service.orderservice;

import projectpackage.model.orders.Category;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface CategoryService extends MessageBook{
    public List<Category> getAllCategories();
    public List<Category> getSimpleCategoryList();
    public Category getSingleCategoryById(Integer id);
    public IUDAnswer deleteCategory(Integer id);
    public IUDAnswer insertCategory(Category category);
    public IUDAnswer updateCategory(Integer id, Category newCategory);
}
