package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.LinksDTO;
import projectpackage.model.auth.User;
import projectpackage.dto.AuthForm;
import projectpackage.service.authservice.UserService;
import projectpackage.service.linksservice.LinksService;
import projectpackage.service.securityservice.SecurityService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Lenovo on 21.05.2017.
 */
@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    @Autowired
    SecurityService securityService;

    @Autowired
    UserService userService;

    @Autowired
    LinksService linksService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> doLogin(@RequestBody AuthForm form, HttpServletRequest request){
        System.out.println("AUTHORIZED!");
        boolean result = (boolean) securityService.autologin(form.getLogin(), form.getPassword());
        if (!result){
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        } else {
            User user = userService.getSingleUserById(securityService.getAuthenticatedUserId(form.getLogin()));
            user.setPassword(null);
            request.getSession().setAttribute("USER", user);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/isauthorized", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String isAuthorized(HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        String role;
        if (null==thisUser){
            role = "NA";
        } else {
            role = thisUser.getRole().getRoleName();
        }
        return role;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/links", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LinksDTO getLinks(HttpServletRequest request){
        return linksService.getLinks();
    }
}
