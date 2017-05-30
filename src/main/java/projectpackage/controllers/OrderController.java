package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.dto.SearchAvailabilityParamsDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.service.orderservice.OrderService;
import projectpackage.service.roomservice.RoomTypeService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Lenovo on 28.05.2017.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    RoomTypeService roomTypeService;

    //Get Order List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "orderList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Order>> getOrderList(){
        List<Order> orders = orderService.getAllOrders();
        List<Resource<Order>> resources = new ArrayList<>();
        for (Order order:orders){
            Resource<Order> orderResource = new Resource<Order>(order);
            orderResource.add(linkTo(methodOn(OrderController.class).getOrder(order.getObjectId(), null)).withSelfRel());
            resources.add(orderResource);
        }
        return resources;
    }

    //Get single Order by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Order>> getOrder(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Order order = orderService.getSingleOrderById(id);
        Resource<Order> resource = new Resource<>(order);
        HttpStatus status;
        if (null!= order){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(OrderController.class).deleteOrder(order.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(OrderController.class).updateOrder(order.getObjectId(), order)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Order>> response = new ResponseEntity<Resource<Order>>(resource, status);
        return response;
    }

    //Create order, fetch into database
    @CacheRemoveAll(cacheName = "orderList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createOrder(@RequestBody Order newOrder){
        IUDAnswer result = orderService.insertOrder(newOrder);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update order method
    @CacheRemoveAll(cacheName = "orderList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateOrder(@PathVariable("id") Integer id, @RequestBody Order changedOrder){
        if (!id.equals(changedOrder.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, "wrongId"), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = orderService.updateOrder(id, changedOrder);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete order method
    @CacheRemoveAll(cacheName = "orderList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteOrder(@PathVariable("id") Integer id){
        IUDAnswer result = orderService.deleteOrder(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Order> createOrderByRoomType(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        List<OrderDTO> dtoData = (List<OrderDTO>) request.getSession().getAttribute("ORDERDATA");
        OrderDTO dto=null;
        for (OrderDTO order:dtoData){
            if (id.equals(order.getRoomTypeId())){
                dto = order;
                break;
            }
        }
//        dto = dtoData.stream().filter(order -> id.equals(order.getRoomTypeId())).findAny();
        Order order = orderService.createOrderTemplate(thisUser,dto);
        request.getSession().removeAttribute("ORDERDATA");
        request.getSession().setAttribute("NEWORDER", order);
        return new ResponseEntity<Order>(order, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/accept", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> acceptOrder(HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        Order order = (Order) request.getSession().getAttribute("NEWORDER");
        request.getSession().removeAttribute("NEWORDER");
        IUDAnswer answer = orderService.insertOrder(order);
        HttpStatus status = HttpStatus.CREATED;
        if (!answer.isSuccessful()){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<IUDAnswer>(answer, status);
    }

    @RequestMapping(value = "/searchavailability", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<OrderDTO>> searchAvailabilityForOrderCreation(@RequestBody SearchAvailabilityParamsDTO searchDto, HttpServletRequest request){
        System.out.println(searchDto.getArrival());
        List<OrderDTO> data = roomTypeService.getRoomTypes(searchDto.getArrival(),searchDto.getDeparture(),searchDto.getLivingPersons(), searchDto.getCategoryId());
        List<OrderDTO> dtoData = data.stream().filter(dto -> dto.isAvailable()).collect(Collectors.toList());
        request.getSession().setAttribute("ORDERDATA", dtoData);
        return new ResponseEntity<List<OrderDTO>>(dtoData, HttpStatus.FOUND);
    }
}
