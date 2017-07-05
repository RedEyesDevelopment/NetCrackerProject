package projectpackage.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.authservice.UserService;
import projectpackage.service.orderservice.CategoryService;
import projectpackage.service.orderservice.OrderService;
import projectpackage.service.roomservice.RoomTypeService;
import projectpackage.service.support.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger LOGGER = Logger.getLogger(OrderController.class);

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
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Order>> getOrderList(HttpServletRequest request){
        List<Order> orders = orderService.getAllOrders();
        List<Resource<Order>> resources = new ArrayList<>();
        for (Order order:orders){
            if (order.getJournalRecords() == null ) {
                order.setJournalRecords(new ArrayList<>());
            }
            Resource<Order> orderResource = new Resource<Order>(order);
            orderResource.add(linkTo(methodOn(OrderController.class).getOrder(order.getObjectId(), null)).withSelfRel());
            resources.add(orderResource);
        }
        return resources;
    }

    //Get Simple Order List
    @ResponseStatus(HttpStatus.OK)
    @JsonView(JacksonMappingMarker.Data.class)
    @GetMapping(value = "/simpleList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Order> getSimpleOrderList(){
        return orderService.getSimpleOrderList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Order>> getOrderListByUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return null;
        }
        List<Order> orders = orderService.getOrdersByClient(user);

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
        HttpStatus status = status = HttpStatus.BAD_REQUEST;;
        IUDAnswer answer = null;
        try {
            answer = orderService.insertOrder(order);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_FIELD, e.getMessage()), status);
        }

        if (answer != null && answer.isSuccessful()){
            status = HttpStatus.OK;
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
        if (null == orders) {
            return new ResponseEntity<List<Order>>((List<Order>) null,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "updateinfo/{id}", method = RequestMethod.PUT, produces = {MediaType
            .APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<FreeRoomsUpdateOrderDTO> getInfoForUpdateOrder(@PathVariable("id") Integer id, @RequestBody ChangeOrderDTO changeOrderDTO, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminReceptionAndData(thisUser, changeOrderDTO, id);
        FreeRoomsUpdateOrderDTO dto = null;
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<FreeRoomsUpdateOrderDTO>(dto, HttpStatus.BAD_REQUEST);
        }
        dto = orderService.getFreeRoomsToUpdateOrder(id, changeOrderDTO);
        request.getSession().setAttribute("CHANGE_DTO", changeOrderDTO);
        return new ResponseEntity<FreeRoomsUpdateOrderDTO>(dto, HttpStatus.OK);
    }

    //Create order, fetch into database
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createOrder(@RequestBody Order newOrder){
        IUDAnswer result = null;
        try {
            result = orderService.insertOrder(newOrder);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<BookedOrderDTO> createOrderByRoomType(@RequestBody BookDTO bookDTO, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        OrderDTO dto = null;
        User client = null;
        if (thisUser == null) {
            return new ResponseEntity<BookedOrderDTO>(new BookedOrderDTO(dto), HttpStatus.BAD_REQUEST);
        }
        if (bookDTO.getRoomTypeId() == null) {
            return new ResponseEntity<BookedOrderDTO>(new BookedOrderDTO(dto), HttpStatus.BAD_REQUEST);
        }
        if (thisUser.getRole().getObjectId() == 3) {
            client = thisUser;
        } else {
            client = new User();
            client.setObjectId(bookDTO.getUserId());
        }
        User lastModificator = thisUser;
        List<OrderDTO> dtoData = (List<OrderDTO>) request.getSession().getAttribute("ORDERDATA");
        for (OrderDTO order : dtoData) {
            if (bookDTO.getRoomTypeId().equals(order.getRoomTypeId())) {
                dto = order;
                break;
            }
        }
        Category category = categoryService.getSingleCategoryById(dto.getCategoryId());
        Order order = orderService.createOrderTemplate(client, lastModificator, dto);
        client = userService.getSingleUserById(client.getObjectId());
        order.setCategory(category);
        request.getSession().removeAttribute("ORDERDATA");
        request.getSession().setAttribute("NEWORDER", order);

        BookedOrderDTO responseDto = new BookedOrderDTO(dto);
        responseDto.setCategoryName(category.getCategoryTitle());
        responseDto.setClient(client.getFirstName() + " " + client.getLastName());
        return new ResponseEntity<BookedOrderDTO>(responseDto, HttpStatus.OK);

    }

    @RequestMapping(value = "/searchavailability", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<OrderDTO>> searchAvailabilityForOrderCreation(@RequestBody SearchAvailabilityParamsDTO searchDto, HttpServletRequest request) throws ParseException {
        List<OrderDTO> data = roomTypeService.getRoomTypes(searchDto.getArrival(),searchDto.getDeparture(),
                searchDto.getLivingPersons(), searchDto.getCategoryId());
        if (data == null || data.isEmpty()) {
            return new ResponseEntity<List<OrderDTO>>(data, HttpStatus.BAD_REQUEST);
        }

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
        User user = new User();
        user.setObjectId(thisUser.getObjectId());
        order.setLastModificator(user);
        try {
            iudAnswer = orderService.updateOrder(id, order);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.OK);
        } else {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "admin/update/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
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

        try {
            iudAnswer = orderService.setNewDataIntoOrder(id, thisUser.getObjectId(), changeOrderDTO, orderDTO);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        }
        request.getSession().removeAttribute("CHANGE_DTO");
        return new ResponseEntity<IUDAnswer>(iudAnswer, status);
    }

    @RequestMapping(value = "user/update/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateOrderForUser(@PathVariable("id") Integer id,
                                                 @RequestBody String comment,
                                                 HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAndData(thisUser, comment, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.getSingleOrderById(id);
        if (order == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_UPDATE_ID), HttpStatus.BAD_REQUEST);
        }
        if (order.getClient().getObjectId() != thisUser.getObjectId()) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, ORDER_DOESNT_BELONG_USER), HttpStatus.BAD_REQUEST);
        }
        order.setComment(comment);

        try {
            iudAnswer = orderService.updateOrder(id, order);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.OK);
        } else {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
    }

    //Delete order method
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteOrder(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(thisUser, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.getSingleOrderById(id);
        Date validDate = new DateTime().plusDays(1).toDate();
        if (order.getLivingStartDate().before(validDate)) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, ORDER_STARTED), HttpStatus.FORBIDDEN);
        }

        try {
            iudAnswer = orderService.deleteOrder(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return new ResponseEntity<IUDAnswer>(iudAnswer, status);
    }
}
