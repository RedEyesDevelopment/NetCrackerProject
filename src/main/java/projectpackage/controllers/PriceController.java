package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.rates.Price;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.rateservice.PriceService;

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
@RequestMapping("/prices")
public class PriceController {
    @Autowired
    PriceService priceService;

    //Get Price List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "priceList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Price>> getPriceList(){
        List<Price> prices = priceService.getAllPrices();
        List<Resource<Price>> resources = new ArrayList<>();
        for (Price price:prices){
            Resource<Price> priceResource = new Resource<Price>(price);
            priceResource.add(linkTo(methodOn(PriceController.class).getPrice(price.getObjectId(), null)).withSelfRel());
            resources.add(priceResource);
        }
        return resources;
    }

    //Get single Price by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Price>> getPrice(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Price price = priceService.getSinglePriceById(id);
        Resource<Price> resource = new Resource<>(price);
        HttpStatus status;
        if (null!=price){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(PriceController.class).deletePrice(price.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(PriceController.class).updatePrice(price.getObjectId(), price)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Price>> response = new ResponseEntity<Resource<Price>>(resource, status);
        return response;
    }

    //Create price, fetch into database
    @CacheRemoveAll(cacheName = "priceList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createPrice(@RequestBody Price newPrice){
        IUDAnswer result = priceService.insertPrice(newPrice);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update price method
    @CacheRemoveAll(cacheName = "priceList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updatePrice(@PathVariable("id") Integer id, @RequestBody Price changedPrice){
        if (!id.equals(changedPrice.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, "wrongId"), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = priceService.updatePrice(id, changedPrice);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete price method
    @CacheRemoveAll(cacheName = "priceList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deletePrice(@PathVariable("id") Integer id){
        IUDAnswer result = priceService.deletePrice(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
