package projectpackage.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.CategoryDTO;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.JacksonMappingMarker;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.orderservice.CategoryService;
import projectpackage.service.support.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;
import static projectpackage.service.MessageBook.NULL_ID;
import static projectpackage.service.MessageBook.WRONG_DELETED_ID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOGGER = Logger.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get Category List
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Category>> getCategoryList(){
        List<Category> categorys = categoryService.getAllCategories();
        List<Resource<Category>> resources = new ArrayList<>();
        for (Category category:categorys){
            if (category.getComplimentaries() == null) {
                category.setComplimentaries(new HashSet<>());
            }
            Resource<Category> categoryResource = new Resource<Category>(category);
            categoryResource.add(linkTo(methodOn(CategoryController.class).getCategory(category.getObjectId(), null)).withSelfRel());
            resources.add(categoryResource);
        }
        return resources;
    }

    //Get Category List
    @JsonView(JacksonMappingMarker.List.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/simpleList", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Category> getSimpleCategoryList(){
        return categoryService.getSimpleCategoryList();
    }

    //Get single Category by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Category>> getCategory(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Category category = categoryService.getSingleCategoryById(id);
        Resource<Category> resource = new Resource<>(category);
        HttpStatus status;
        if (null!=category){
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Category>> response = new ResponseEntity<Resource<Category>>(resource, status);
        return response;
    }

    //Create category, fetch into database
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createCategory(@RequestBody CategoryDTO categoryDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, categoryDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Category category = new Category();
        category.setCategoryTitle(categoryDTO.getCategoryTitle());
        category.setCategoryPrice(categoryDTO.getCategoryPrice());

        try {
            iudAnswer = categoryService.insertCategory(category);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

    //Update category method
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateCategory(@PathVariable("id") Integer id, @RequestBody CategoryDTO categoryDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, categoryDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Category category = new Category();
        category.setCategoryTitle(categoryDTO.getCategoryTitle());
        category.setCategoryPrice(categoryDTO.getCategoryPrice());

        try {
            iudAnswer = categoryService.updateCategory(id, category);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

    //Delete category method
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteCategory(@PathVariable("id") Integer id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }

        try {
            iudAnswer = categoryService.deleteCategory(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return new ResponseEntity<IUDAnswer>(iudAnswer, status);
    }
}
