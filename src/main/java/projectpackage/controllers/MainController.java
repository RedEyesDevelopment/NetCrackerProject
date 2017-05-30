package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.service.roomservice.RoomTypeService;

import java.util.List;
import java.util.Map;

/**
 * Created by Arizel on 30.05.2017.
 */
@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    RoomTypeService roomTypeService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<Map<String, Object>>> getRoomTypesWithCost() {
        //ResponseEntity<List<Map<String, Object>>> roomTypes = roomTypeService.getRoomTypes()
        return null;
    }

}
