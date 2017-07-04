package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.AuthFlagDTO;
import projectpackage.dto.AuthForm;
import projectpackage.dto.LinksDTO;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;
import projectpackage.service.linksservice.LinksService;
import projectpackage.service.securityservice.SecurityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
        ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        if (null != form.getLogin()) {
            User user = userService.getSingleUserByUsername(form.getLogin());
            if (null != user) {
                if (!user.getEnabled()){
                    return response;
                }
            } else {
                return response;
            }
        }
        boolean result = securityService.autologin(form.getLogin(), form.getPassword());
        if (!result){
            return response;
        } else {
            User user = userService.getSingleUserById(securityService.getAuthenticatedUserId(form.getLogin()));
            user.setPassword(null);
            request.getSession().setAttribute("USER", user);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/isauthorized", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public AuthFlagDTO isAuthorized(HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        AuthFlagDTO result;
        if (null==thisUser){
            result = new AuthFlagDTO("NA");
        } else {
            result = new AuthFlagDTO(thisUser.getRole().getRoleName());
        }
        return result;
    }

    @RequestMapping(value = "/links", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LinksDTO getLinks(){
        return linksService.getLinks();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> doLogout(HttpServletRequest request) throws ServletException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user != null) {
            request.logout();
            request.getSession().invalidate();
        } else {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
