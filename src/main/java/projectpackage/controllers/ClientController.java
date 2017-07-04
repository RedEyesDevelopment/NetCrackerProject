package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.MailDTO;
import projectpackage.model.auth.User;
import projectpackage.service.fileservice.mails.MailService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    MailService mailService;

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> sendMessage(@RequestBody MailDTO mailDTO, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer;
        if (null!= mailDTO.getMessage()){
            if (null!=mailDTO.getThemeMessage()){
                mailService.sendEmailToMyself(thisUser, mailDTO.getThemeMessage(), mailDTO.getMessage());
            } else {
                mailService.sendEmailToMyself(thisUser, "Message from a client", mailDTO.getMessage());
            }
            iudAnswer = new IUDAnswer(true);
        } else {
            iudAnswer = new IUDAnswer(false);
        }
        return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.OK);
    }
}