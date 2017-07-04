package projectpackage.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.rates.Price;
import projectpackage.service.rateservice.PriceService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.WRONG_FIELD;

/**
 * Created by Lenovo on 28.05.2017.
 */
@RestController
@RequestMapping("/prices")
public class PriceController {

    private static final Logger LOGGER = Logger.getLogger(PriceController.class);

    @Autowired
    PriceService priceService;

    @Autowired
    ServiceUtils serviceUtils;

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
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Price>> getPrice(@PathVariable("id") Integer id, HttpServletRequest request){
        Price price = priceService.getSinglePriceById(id);
        Resource<Price> resource = new Resource<>(price);
        HttpStatus status;
        if (null!=price){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Price>> response = new ResponseEntity<Resource<Price>>(resource, status);
        return response;
    }

    //Update price method
    @CacheRemoveAll(cacheName = "priceList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updatePrice(@PathVariable("id") Integer id, @RequestBody Price changedPrice, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, changedPrice, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        try {
            iudAnswer = priceService.updatePrice(id, changedPrice);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

}
