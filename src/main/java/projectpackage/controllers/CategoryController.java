package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.orderservice.CategoryService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Lenovo on 28.05.2017.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    //Get Category List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "categoryList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Category>> getCategoryList(){
        List<Category> categorys = categoryService.getAllCategories();
        List<Resource<Category>> resources = new ArrayList<>();
        for (Category category:categorys){
            Resource<Category> categoryResource = new Resource<Category>(category);
            categoryResource.add(linkTo(methodOn(CategoryController.class).getCategory(category.getObjectId(), null)).withSelfRel());
            resources.add(categoryResource);
        }
        return resources;
    }

    //Get single Category by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Category>> getCategory(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Category category = categoryService.getSingleCategoryById(id);
        Resource<Category> resource = new Resource<>(category);
        HttpStatus status;
        if (null!=category){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(CategoryController.class).deleteCategory(category.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(CategoryController.class).updateCategory(category.getObjectId(), category)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Category>> response = new ResponseEntity<Resource<Category>>(resource, status);
        return response;
    }

    //Create category, fetch into database
    @CacheRemoveAll(cacheName = "categoryList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createCategory(@RequestBody Category newCategory){
        IUDAnswer result = categoryService.insertCategory(newCategory);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update category method
    @CacheRemoveAll(cacheName = "categoryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateCategory(@PathVariable("id") Integer id, @RequestBody Category changedCategory){
        if (!id.equals(changedCategory.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, "wrongId"), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = categoryService.updateCategory(id, changedCategory);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete category method
    @CacheRemoveAll(cacheName = "categoryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteCategory(@PathVariable("id") Integer id){
        IUDAnswer result = categoryService.deleteCategory(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
