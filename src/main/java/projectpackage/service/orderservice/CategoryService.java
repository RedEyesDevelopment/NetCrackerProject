package projectpackage.service.orderservice;

import projectpackage.model.orders.Category;
import projectpackage.dto.IUDAnswer;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface CategoryService {

    public List<Category> getAllCategories();
    public List<Category> getAllCategories(String orderingParameter, boolean ascend);
    public Category getSingleCategoryById(int id);
    public IUDAnswer deleteCategory(int id);
    public IUDAnswer insertCategory(Category category);
    public IUDAnswer updateCategory(int id, Category newCategory);
}
