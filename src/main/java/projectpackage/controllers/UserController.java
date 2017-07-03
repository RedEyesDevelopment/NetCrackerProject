package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.UserBasicDTO;
import projectpackage.dto.UserDTO;
import projectpackage.dto.UserPasswordDTO;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;
import projectpackage.service.securityservice.SecurityService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.NULL_ENTITY;
import static projectpackage.service.MessageBook.WRONG_PASSWORD;
import static projectpackage.service.MessageBook.WRONG_UPDATE_ID;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	SecurityService securityService;

	@Autowired
    ServiceUtils serviceUtils;

	//Get User List
	@ResponseStatus(HttpStatus.OK)
	@CacheResult(cacheName = "userList")
	@GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public List<Resource<User>> getUserList() {
		List<User> users = userService.getAllUsers();
		List<Resource<User>> resources = new ArrayList<>();
		for (User user : users) {
			Resource<User> userResource = new Resource<User>(user);
			userResource.add(linkTo(methodOn(UserController.class).getUser(user.getObjectId(), null)).withSelfRel());
			resources.add(userResource);
		}
		return resources;
	}

	//Get single User by id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<Resource<User>> getUser(@PathVariable("id") Integer id, HttpServletRequest request) {
		User thisUser = (User) request.getSession().getAttribute("USER");
        User user = userService.getSingleUserById(id);
		Resource<User> resource = new Resource<>(user);
		HttpStatus status;
		if (null != thisUser) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		ResponseEntity<Resource<User>> response = new ResponseEntity<Resource<User>>(resource, status);
		return response;
	}

	//Create user, fetch into database
	@CacheRemoveAll(cacheName = "userList")
	@RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> createUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
	    User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(sessionUser, userDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        Set<Phone> phones = new HashSet<>();
        Phone phone = new Phone();
        phone.setPhoneNumber(userDTO.getPhoneNumber());
        phones.add(phone);
        Role role = new Role();
        role.setObjectId(userDTO.getRoleId());
        newUser.setRole(role);
        newUser.setPhones(phones);
        newUser.setPassword(userDTO.getPassword());
        newUser.setAdditionalInfo(userDTO.getAdditionalInfo());
        newUser.setEmail(userDTO.getEmail());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEnabled(Boolean.TRUE);
		IUDAnswer result = userService.insertUser(newUser);
		HttpStatus status;
		if (result.isSuccessful()) status = HttpStatus.OK;
        else status = HttpStatus.BAD_REQUEST;
		ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
		return responseEntity;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = {MediaType
			.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> registrationUser(@RequestBody User newUser,HttpServletRequest request) {
        if (newUser == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }
        Role role = new Role();
        role.setObjectId(3);
        role.setRoleName("CLIENT");
        newUser.setRole(role);
        newUser.setEnabled(Boolean.TRUE);
        IUDAnswer result = userService.insertUser(newUser);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
	}

	//Update user method
	@CacheRemoveAll(cacheName = "userList")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, userDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        User changedUser = new User();
        Set<Phone> phones = new HashSet<>();
        Phone phone = new Phone();
        phone.setPhoneNumber(userDTO.getPhoneNumber());
        phones.add(phone);
        Role role = new Role();
        role.setObjectId(userDTO.getRoleId());
        changedUser.setEnabled(userDTO.getEnabled());
        changedUser.setFirstName(userDTO.getFirstName());
        changedUser.setLastName(userDTO.getLastName());
        changedUser.setEmail(userDTO.getEmail());
        changedUser.setAdditionalInfo(userDTO.getAdditionalInfo());
        changedUser.setPassword(userDTO.getPassword());
        changedUser.setRole(role);
        changedUser.setPhones(phones);
		IUDAnswer result = userService.updateUser(id, changedUser);
		HttpStatus status;
		if (result.isSuccessful()) status = HttpStatus.OK;
        else status = HttpStatus.BAD_REQUEST;
		ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
		return responseEntity;
	}

	@RequestMapping(value = "/update/password/{id}", method = RequestMethod.PUT,
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> updatePassword(@PathVariable("id") Integer id,
													@RequestBody UserPasswordDTO userPasswordDTO, HttpServletRequest request) {
		User sessionUser = (User) request.getSession().getAttribute("USER");
		IUDAnswer iudAnswer = serviceUtils.checkForChangePassword(sessionUser, userPasswordDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
		return updatePassword(request, id, userPasswordDTO);
	}

    @RequestMapping(value = "admin/update/password/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateAdminPassword(@PathVariable("id") Integer id,
                                                    @RequestBody UserPasswordDTO userPasswordDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkAdminForChangePassword(sessionUser, userPasswordDTO, id);
        if (!iudAnswer.isSuccessful() && sessionUser.getObjectId() == id.intValue()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        return updatePassword(request, id, userPasswordDTO);
    }

	@RequestMapping(value = "/update/basic/{id}", method = RequestMethod.PUT,
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> updateBasicInfo(@PathVariable("id") Integer id, @RequestBody UserBasicDTO userBasicDTO, HttpServletRequest request) {
		User sessionUser = (User) request.getSession().getAttribute("USER");
		IUDAnswer iudAnswer = serviceUtils.checkSessionAndData(sessionUser, userBasicDTO, id);
		if (!iudAnswer.isSuccessful()) {
		    return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        } else if (id != sessionUser.getObjectId()) {
			return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_UPDATE_ID), HttpStatus.BAD_REQUEST);
		}
		User user = userService.getSingleUserById(id);
		user.setFirstName(userBasicDTO.getFirstName());
		user.setLastName(userBasicDTO.getLastName());
		user.setEmail(userBasicDTO.getEmail());
		user.setAdditionalInfo(userBasicDTO.getAdditionalInfo());
		IUDAnswer answer = userService.updateUser(id, user);
		if (answer.isSuccessful()) {
			request.getSession().removeAttribute("USER");
			request.getSession().setAttribute("USER", userService.getSingleUserById(id));
			return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
		}

		return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "admin/update/basic/{id}", method = RequestMethod.PUT,
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> updateAdminBasicInfo(@PathVariable("id") Integer id, @RequestBody UserBasicDTO userBasicDTO, HttpServletRequest request) {
		User sessionUser = (User) request.getSession().getAttribute("USER");
		IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(sessionUser, userBasicDTO, id);
		if (!iudAnswer.isSuccessful()) {
			return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
		}
		User user = userService.getSingleUserById(id);
		user.setFirstName(userBasicDTO.getFirstName());
		user.setLastName(userBasicDTO.getLastName());
		user.setEmail(userBasicDTO.getEmail());
		user.setAdditionalInfo(userBasicDTO.getAdditionalInfo());
		IUDAnswer answer = userService.updateUser(id, user);
		if (answer.isSuccessful() && sessionUser.getObjectId() == id.intValue()) {
			request.getSession().removeAttribute("USER");
			request.getSession().setAttribute("USER", userService.getSingleUserById(id));
			return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
		}

		return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
	}

	//Delete user method
	@CacheRemoveAll(cacheName = "userList")
	@RequestMapping(value = "delete/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> deleteUser(@PathVariable("id") Integer id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("USER");
		IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
		if (!iudAnswer.isSuccessful()) {
		    return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
		IUDAnswer result = userService.deleteUser(id);
		HttpStatus status;
		if (result.isSuccessful()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.NOT_FOUND;
		}
		ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
		return responseEntity;
	}

    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(value = "restore/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> restoreUser(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        IUDAnswer result = userService.restoreUser(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

	//returned user-client from session
	@RequestMapping(value = "/myself")
	public ResponseEntity<User> getUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("USER");
		if (user == null) return new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
		Integer userId = user.getObjectId();
		HttpStatus status;
		user = userService.getSingleUserById(userId);
		user.setPassword(null);
		status = HttpStatus.OK;

		return new ResponseEntity<User>(user, status);
	}

	private ResponseEntity<IUDAnswer> updatePassword(HttpServletRequest request, Integer id, UserPasswordDTO userPasswordDTO) {
        User user = userService.getSingleUserById(id);

        if (!securityService.isPasswordMatchEncrypted(userPasswordDTO.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_PASSWORD), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(userPasswordDTO.getNewPassword());
        IUDAnswer answer = userService.updateUserPassword(id, user);
        if (answer.isSuccessful()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(id));
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        } else {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
        }
    }
}
