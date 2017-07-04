package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.orders.Category;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;
import projectpackage.repository.ordersdao.CategoryDAO;

import java.util.List;

@Service
@Log4j
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class);

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ComplimentaryDAO complimentaryDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        if (null == categories) {
            LOGGER.info("Returned NULL!!!");
        }
        return categories;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Category> getSimpleCategoryList() {
        return categoryDAO.getSimpleCategoryList();
    }

    @Transactional(readOnly = true)
    @Override
    public Category getSingleCategoryById(Integer id) {
        Category category = categoryDAO.getCategory(id);
        if (null == category) {
            LOGGER.info("Returned NULL!!!");
        }
        return category;
    }

    @Transactional
    @Override
    public IUDAnswer deleteCategory(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        categoryDAO.deleteCategory(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertCategory(Category category) {
        Integer categoryId = null;

        categoryId = categoryDAO.insertCategory(category);

        return new IUDAnswer(categoryId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateCategory(Integer id, Category newCategory) {
        if (newCategory == null) {
            return null;
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        newCategory.setObjectId(id);
        Category oldCategory = categoryDAO.getCategory(id);
        categoryDAO.updateCategory(newCategory, oldCategory);

        return new IUDAnswer(id,true);
    }
}
