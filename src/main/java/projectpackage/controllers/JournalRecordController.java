package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.JournalRecordDTO;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.service.MessageBook;
import projectpackage.service.maintenanceservice.JournalRecordService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.NEED_TO_AUTH;
import static projectpackage.service.MessageBook.NOT_RECEPTION_OR_ADMIN;
import static projectpackage.service.MessageBook.NULL_ENTITY;

/**
 * Created by Arizel on 28.05.2017.
 */
@RestController
@RequestMapping("/journalRecords")
public class JournalRecordController {
    @Autowired
    JournalRecordService journalRecordService;

    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "journalRecordList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<JournalRecord>> getJournalRecordList() {
        List<JournalRecord> JournalRecords = journalRecordService.getAllJournalRecords();
        List<Resource<JournalRecord>> resources = new ArrayList<>(JournalRecords.size());

        for (JournalRecord JournalRecord : JournalRecords) {
            Resource<JournalRecord> resource = new Resource<>(JournalRecord);
            resource.add(linkTo(methodOn(JournalRecordController.class).getJournalRecord(JournalRecord.getObjectId(), null)).withSelfRel());
            resources.add(resource);
        }

        return resources;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<JournalRecord>> getJournalRecord(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");

        JournalRecord JournalRecord = journalRecordService.getSingleEntityById(id);
        Resource<JournalRecord> resource = new Resource<>(JournalRecord);
        HttpStatus status;
        if (null != JournalRecord) {
            if (thisUser.getRole().getRoleName().equals("ADMIN")) {
                resource.add(linkTo(methodOn(JournalRecordController.class).deleteJournalRecord(JournalRecord.getObjectId())).withRel("delete"));
            }
            resource.add(linkTo(methodOn(JournalRecordController.class).updateJournalRecord(JournalRecord.getObjectId(), JournalRecord)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Resource<JournalRecord>> response = new ResponseEntity<Resource<JournalRecord>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "journalRecordList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createJournalRecord(@RequestBody JournalRecordDTO journalRecordDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        if (sessionUser == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (sessionUser.getRole().getObjectId() == 3) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_RECEPTION_OR_ADMIN), HttpStatus.BAD_REQUEST);
        } else if (journalRecordDTO == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }
        JournalRecord newJournalRecord = new JournalRecord();
        newJournalRecord.setOrderId(journalRecordDTO.getOrderId());
        newJournalRecord.setUsedDate(new Date());
        newJournalRecord.setCount(journalRecordDTO.getCount());
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(journalRecordDTO.getMaintenanceId());
        newJournalRecord.setMaintenance(maintenance);
        IUDAnswer result = journalRecordService.insertJournalRecord(newJournalRecord);

        HttpStatus status = result.isSuccessful() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "journalRecordList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateJournalRecord(@PathVariable("id") Integer id, @RequestBody JournalRecord changedJournalRecord) {
        if (!id.equals(changedJournalRecord.getObjectId())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        IUDAnswer result = journalRecordService.updateJournalRecord(id, changedJournalRecord);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "journalRecordList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteJournalRecord(@PathVariable("id") Integer id) {
        IUDAnswer result = journalRecordService.deleteJournalRecord(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<IUDAnswer>(result, status);
    }
}
