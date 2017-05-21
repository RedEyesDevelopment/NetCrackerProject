package projectpackage.service.orderservice;

import projectpackage.model.orders.Category;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface CategoryService {

    public List<Category> getAllCategories();
    public List<Category> getAllCategories(String orderingParameter, boolean ascend);
    public Category getSingleCategoryById(int id);
    public boolean deleteCategory(int id);
    public boolean insertCategory(Category category);
    public boolean updateCategory(int id, Category newCategory);
}
