package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.PeriodDTO;
import projectpackage.dto.RoomStatDTO;
import projectpackage.service.roomservice.RoomService;

/**
 * Created by Arizel on 01.06.2017.
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    RoomService roomService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<RoomStatDTO> getRoomStatistic(@RequestBody PeriodDTO periodDTO) {
        RoomStatDTO roomStatDTO = roomService.getAllRoomsOnPeriod(periodDTO.getStartDate(), periodDTO.getFinishDate());
        HttpStatus status = roomStatDTO != null ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;
        return new ResponseEntity<RoomStatDTO>(roomStatDTO, status);
    }

}
