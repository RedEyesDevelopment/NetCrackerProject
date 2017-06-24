package projectpackage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.MailDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Lenovo on 08.06.2017.
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> sendMessage(@RequestBody MailDTO mailDTO, HttpServletRequest request) {
        System.out.println(mailDTO);
        IUDAnswer iudAnswer = new IUDAnswer(true);
        return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.OK);
    }
}