package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.BlockDTO;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.service.blockservice.BlockService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

@RestController
@RequestMapping("/blocks")
public class BlockController {

    @Autowired
    BlockService blockService;

    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "blockList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Block>> getBlockList() {
        List<Block> blocks = blockService.getAllBlocks();
        List<Resource<Block>> resources = new ArrayList<>(blocks.size());

        for (Block block : blocks) {
            Resource<Block> resource = new Resource<>(block);
            resource.add(linkTo(methodOn(BlockController.class).getBlock(block.getObjectId(), null)).withSelfRel());
            resources.add(resource);
        }

        return resources;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Block>> getBlock(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");

        Block block = blockService.getSingleBlockById(id);
        Resource<Block> resource = new Resource<>(block);
        HttpStatus status;
        if (null != block) {
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Resource<Block>> response = new ResponseEntity<Resource<Block>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "blockList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createBlock(@RequestBody BlockDTO blockDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (user.getRole().getObjectId() != 1) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_ADMIN), HttpStatus.BAD_REQUEST);
        }
        Room room = new Room();
        room.setObjectId(blockDTO.getRoomId());
        Block block = new Block();
        block.setBlockStartDate(blockDTO.getBlockStartDate());
        block.setBlockFinishDate(blockDTO.getBlockFinishDate());
        block.setReason(blockDTO.getReason());
        block.setRoom(room);

        IUDAnswer result = blockService.insertBlock(block);

        HttpStatus status = result.isSuccessful() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "blockList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateBlock(@PathVariable("id") Integer id, @RequestBody BlockDTO blockDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        if (sessionUser == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (sessionUser.getRole().getObjectId() != 1) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_ADMIN), HttpStatus.BAD_REQUEST);
        } else if (id == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ID), HttpStatus.BAD_REQUEST);
        }

        Room room = new Room();
        room.setObjectId(blockDTO.getRoomId());
        Block block = new Block();
        block.setReason(blockDTO.getReason());
        block.setRoom(room);
        block.setBlockStartDate(blockDTO.getBlockStartDate());
        block.setBlockFinishDate(blockDTO.getBlockFinishDate());
        IUDAnswer result = blockService.updateBlock(id, block);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "blockList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteBlock(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        if (id == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ID), HttpStatus.BAD_REQUEST);
        } else if (user.getRole().getObjectId() != 1) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_ADMIN), HttpStatus.BAD_REQUEST);
        } else if (null == user) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        }

        IUDAnswer result = blockService.deleteBlock(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

}
