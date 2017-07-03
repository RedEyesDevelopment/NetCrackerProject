package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.LoginDTO;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;
import projectpackage.service.linksservice.PasswordChangeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cpass")
public class ChangePasswordController {

    @Autowired
    PasswordChangeService passwordChangeService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{dynamic}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<String> changePassword(@PathVariable("dynamic") String dynamic, HttpServletRequest request) throws ServletException   {
        boolean result = passwordChangeService.verifyDynamicLink(dynamic);
        StringBuilder resultString;
        HttpStatus status;

        resultString = new StringBuilder(new StringBuilder().append("<html>\n").append("<style>\n").append("body{\n").append("background-color: #0000ff;\n").append("font-family: Lucida Console;\n").append("font-size: 56px;\n").append("font-style: normal;\n").append("font-variant: normal;\n").append("color: white;\n").append("</style>\n").append("<body>\n").append("<div style=\"text-align: center;\">"));
        if (result){
            status = HttpStatus.OK;
            resultString.append("Success :)");
        } else {
            status = HttpStatus.BAD_REQUEST;
            resultString.append("Fail :(");
        }
        resultString.append("</div>\n</body>\n</html>\n");

        return new ResponseEntity<String>(resultString.toString(), status);
    }

    @RequestMapping(value = "/for", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> changeFor(@RequestBody LoginDTO login) throws ServletException {
        User user = userService.getSingleUserByUsername(login.getEmail());
        if (null!=user){
            passwordChangeService.createPasswordChangeTarget(user);
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.BAD_REQUEST);
        }
    }
}
