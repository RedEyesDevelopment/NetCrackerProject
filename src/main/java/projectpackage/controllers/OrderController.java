package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.*;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.service.authservice.UserService;
import projectpackage.service.orderservice.CategoryService;
import projectpackage.service.orderservice.OrderService;
import projectpackage.service.roomservice.RoomTypeService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.EMPTY_DTO_IN_SESSION;

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

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get Order List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "orderList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Order>> getOrderList(HttpServletRequest request){
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
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Order>> getOrder(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Order order = orderService.getSingleOrderById(id);
        Resource<Order> resource = new Resource<>(order);
        HttpStatus status = null;
        if (null!= order){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Order>> response = new ResponseEntity<Resource<Order>>(resource, status);
        return response;
    }


    @RequestMapping(value = "/accept", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> acceptOrder(HttpServletRequest request) {
        Order order = (Order) request.getSession().getAttribute("NEWORDER");
        request.getSession().removeAttribute("NEWORDER");
        IUDAnswer answer = orderService.insertOrder(order);
        HttpStatus status = HttpStatus.OK;
        if (!answer.isSuccessful()){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<IUDAnswer>(answer, status);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> cancelOrder(HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        request.getSession().removeAttribute("NEWORDER");
        IUDAnswer answer = new IUDAnswer(true, "orderCanceled");

        return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
    }

    @RequestMapping(value = "/byUser",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<Order>> getAllOrdersByUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        List<Order> orders = orderService.getOrdersByClient(user);
        if (null == orders) return new ResponseEntity<List<Order>>((List<Order>) null,HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "updateinfo/{id}", method = RequestMethod.PUT, produces = {MediaType
            .APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<FreeRoomsUpdateOrderDTO> getInfoForUpdateOrder(@PathVariable("id") Integer id, @RequestBody ChangeOrderDTO changeOrderDTO, HttpServletRequest request) {
        System.out.println("*****************************************");
        System.out.println(changeOrderDTO);
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(thisUser, changeOrderDTO, id);
        FreeRoomsUpdateOrderDTO dto = null;
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<FreeRoomsUpdateOrderDTO>(dto, HttpStatus.BAD_REQUEST);
        }
        dto = orderService.getFreeRoomsToUpdateOrder(id, changeOrderDTO);
        request.getSession().setAttribute("CHANGE_DTO", changeOrderDTO);
        return new ResponseEntity<FreeRoomsUpdateOrderDTO>(dto, HttpStatus.OK);
    }

    //Create order, fetch into database
    @CacheRemoveAll(cacheName = "orderList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createOrder(@RequestBody Order newOrder){
        IUDAnswer result = orderService.insertOrder(newOrder);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<BookedOrderDTO> createOrderByRoomType(@RequestBody BookDTO bookDTO, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        OrderDTO dto = null;
        User client = null;
        if (thisUser == null) return new ResponseEntity<BookedOrderDTO>(new BookedOrderDTO(dto), HttpStatus.BAD_REQUEST);
        if (bookDTO.getRoomTypeId() == null) return new ResponseEntity<BookedOrderDTO>(new BookedOrderDTO(dto), HttpStatus.BAD_REQUEST);
        if (thisUser.getRole().getObjectId() == 3) {
            client = thisUser;
        } else {
            client = new User();
            client.setObjectId(bookDTO.getUserId());
        }
        List<OrderDTO> dtoData = (List<OrderDTO>) request.getSession().getAttribute("ORDERDATA");
        for (OrderDTO order : dtoData) {
            if (bookDTO.getRoomTypeId().equals(order.getRoomTypeId())) {
                dto = order;
                break;
            }
        }
        Category category = categoryService.getSingleCategoryById(dto.getCategoryId());
        Order order = orderService.createOrderTemplate(client, dto);
        order.setCategory(category);
        request.getSession().removeAttribute("ORDERDATA");
        request.getSession().setAttribute("NEWORDER", order);

        BookedOrderDTO responseDto = new BookedOrderDTO(dto);
        responseDto.setCategoryName(category.getCategoryTitle());
        responseDto.setClient(new StringBuilder(thisUser.getFirstName()).append(thisUser.getLastName()).toString());
        return new ResponseEntity<BookedOrderDTO>(responseDto, HttpStatus.OK);

    }

    @RequestMapping(value = "/searchavailability", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<OrderDTO>> searchAvailabilityForOrderCreation(@RequestBody SearchAvailabilityParamsDTO searchDto, HttpServletRequest request) throws ParseException {
        List<OrderDTO> data = roomTypeService.getRoomTypes(searchDto.getArrival(),searchDto.getDeparture(),
                searchDto.getLivingPersons(), searchDto.getCategoryId());
        if (data == null || data.isEmpty()) return new ResponseEntity<List<OrderDTO>>(data, HttpStatus.BAD_REQUEST);

        ResponseEntity<List<OrderDTO>> responseEntity = new ResponseEntity<List<OrderDTO>>(data, HttpStatus.OK);
        List<OrderDTO> dtoData = data.stream().filter(dto -> dto.isAvailable()).collect(Collectors.toList());
        request.getSession().setAttribute("ORDERDATA", dtoData);
        return responseEntity;
    }


    @RequestMapping(value = "admin/confirm/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> changePaidAndConfirm(@PathVariable("id") Integer id, @RequestBody ConfirmPaidOrderDTO confirmPaidOrderDTO, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(thisUser, confirmPaidOrderDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.getSingleOrderById(id);
        order.setIsConfirmed(confirmPaidOrderDTO.getIsConfirmed());
        order.setIsPaidFor(confirmPaidOrderDTO.getIsPaidFor());
        IUDAnswer answer = orderService.updateOrder(id, order);
        if (!answer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateOrder(@PathVariable("id") Integer id,
                                                               @RequestBody OrderDTO orderDTO,
                                                               HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(thisUser, orderDTO, id);
        ChangeOrderDTO changeOrderDTO = (ChangeOrderDTO) request.getSession().getAttribute("CHANGE_DTO");
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        } else if (changeOrderDTO == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, EMPTY_DTO_IN_SESSION), HttpStatus.BAD_REQUEST);
        }

        IUDAnswer answer = orderService.setNewDataIntoOrder(id, thisUser.getObjectId(), changeOrderDTO, orderDTO);
        return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
    }

    //Delete order method
    @CacheRemoveAll(cacheName = "orderList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteOrder(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(thisUser, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        IUDAnswer result = orderService.deleteOrder(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
