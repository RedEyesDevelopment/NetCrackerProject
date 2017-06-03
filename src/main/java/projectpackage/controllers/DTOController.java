package projectpackage.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.dto.SearchAvailabilityParamsDTO;

/**
 * Created by Lenovo on 03.06.2017.
 */
@RestController
@RequestMapping("/dto")
public class DTOController {

    @RequestMapping(value = "/searchavailability", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SearchAvailabilityParamsDTO getSearchForm(){
        return new SearchAvailabilityParamsDTO();
    }
}
