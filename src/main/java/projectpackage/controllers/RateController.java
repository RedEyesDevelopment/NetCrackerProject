package projectpackage.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.RateDTO;
import projectpackage.model.auth.User;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.service.rateservice.RateService;
import projectpackage.service.support.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.WRONG_FIELD;

@RestController
@RequestMapping("/rates")
public class RateController {

    private static final Logger LOGGER = Logger.getLogger(RateController.class);

    @Autowired
    RateService rateService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get Rate List
    @ResponseStatus(HttpStatus.OK)
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Rate>> getRate(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Rate rate = rateService.getSingleRateById(id);
        Resource<Rate> resource = new Resource<>(rate);
        HttpStatus status;
        if (null != rate){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Rate>> result = new ResponseEntity<Resource<Rate>>(resource, status);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createRate(@RequestBody RateDTO rateDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, rateDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Set prices = new HashSet();
        Price price1 = new Price();
        price1.setNumberOfPeople(1);
        price1.setRate(rateDTO.getPriceForOne());
        Price price2 = new Price();
        price2.setNumberOfPeople(2);
        price2.setRate(rateDTO.getPriceForTwo());
        Price price3 = new Price();
        price3.setNumberOfPeople(3);
        price3.setRate(rateDTO.getPriceForThree());
        prices.add(price1);
        prices.add(price2);
        prices.add(price3);
        Rate rate = new Rate();
        rate.setRoomTypeId(rateDTO.getRoomTypeId());
        rate.setRateFromDate(rateDTO.getRateFromDate());
        rate.setRateToDate(rateDTO.getRateToDate());
        rate.setPrices(prices);

        try {
            iudAnswer = rateService.insertRate(rate);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<IUDAnswer>(iudAnswer, status);
    }

}
