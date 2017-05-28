package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.rates.Rate;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.rateservice.RateService;

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
@RequestMapping("/rates")
public class RateController {

    @Autowired
    RateService rateService;

    //Get Rate List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "rateList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Rate>> getRateList(){
        List<Rate> rates = rateService.getAllRates();
        List<Resource<Rate>> resources = new ArrayList<>();
        for (Rate rate:rates){
            Resource<Rate> rateResource = new Resource<Rate>(rate);
            rateResource.add(linkTo(methodOn(RateController.class).getRate(rate.getObjectId(), null)).withSelfRel());
            resources.add(rateResource);
        }
        return resources;
    }

    //Get single Rate by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Rate>> getRate(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Rate rate = rateService.getSingleRateById(id);
        Resource<Rate> resource = new Resource<>(rate);
        HttpStatus status;
        if (null != rate){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(RateController.class).deleteRate(rate.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(RateController.class).updateRate(rate.getObjectId(), rate)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Rate>> result = new ResponseEntity<Resource<Rate>>(resource, status);
        return result;
    }

    //Create rate, fetch into database
    @CacheRemoveAll(cacheName = "rateList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createRate(@RequestBody Rate newRate){
        IUDAnswer result = rateService.insertRate(newRate);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update rate method
    @CacheRemoveAll(cacheName = "rateList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateRate(@PathVariable("id") Integer id, @RequestBody Rate changedRate){
        if (!id.equals(changedRate.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, "wrongId"), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = rateService.updateRate(id, changedRate);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete rate method
    @CacheRemoveAll(cacheName = "rateList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteRate(@PathVariable("id") Integer id){
        IUDAnswer result = rateService.deleteRate(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
