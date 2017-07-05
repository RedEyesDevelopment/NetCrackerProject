package projectpackage.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.*;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.DuplicateEmailException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.authservice.UserService;
import projectpackage.service.securityservice.SecurityService;
import projectpackage.service.support.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
	UserService userService;

	@Autowired
	SecurityService securityService;

	@Autowired
    ServiceUtils serviceUtils;

	//Get User List
	@ResponseStatus(HttpStatus.OK)
//    @JsonView(JacksonMappingMarker.List.class)
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

        iudAnswer = registerUser(newUser);

		HttpStatus status;
		if (iudAnswer != null && iudAnswer.isSuccessful()) {
		    status = HttpStatus.OK;
        } else {
		    status = HttpStatus.BAD_REQUEST;
        }
		return new ResponseEntity<IUDAnswer>(iudAnswer, status);
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = {MediaType
			.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> registrationUser(@RequestBody User newUser) {
        if (newUser == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }
        Role role = new Role();
        role.setObjectId(3);
        role.setRoleName("CLIENT");
        newUser.setRole(role);
        newUser.setEnabled(Boolean.TRUE);
        IUDAnswer result = registerUser(newUser);
        HttpStatus status;
        if (result != null && result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
	}

	//Update user method
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
        iudAnswer = updateUser(id, user);
		HttpStatus status;
		if (iudAnswer != null && iudAnswer.isSuccessful()) {
		    status = HttpStatus.OK;
        } else {
		    status = HttpStatus.BAD_REQUEST;
        }
		ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
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
        User user = userService.getSingleUserById(id);
        if (!securityService.isPasswordMatchEncrypted(userPasswordDTO.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_PASSWORD), HttpStatus.BAD_REQUEST);
        }
        user.setPassword(userPasswordDTO.getNewPassword());
        IUDAnswer answer = changePassword(id, user);

        if (answer != null && answer.isSuccessful()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(id));
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        } else {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
        }
	}

    @RequestMapping(value = "admin/update/password/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateAdminPassword(@PathVariable("id") Integer id,
                                                         @RequestBody UserPasswordDTO userPasswordDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkAdminForChangePassword(sessionUser, userPasswordDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }

        User user = userService.getSingleUserById(id);

        if (!securityService.isPasswordMatchEncrypted(userPasswordDTO.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_PASSWORD), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(userPasswordDTO.getNewPassword());
        IUDAnswer answer = changePassword(id, user);
        return checkUpdateForAdmin(answer, request, sessionUser, id);
    }

    @RequestMapping(value = "admin/update/role/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateUserRole(@PathVariable("id") Integer id,
                                                    @RequestBody RoleDTO roleDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(sessionUser, roleDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        } else if (roleDTO.getRoleId() == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }
        User user = userService.getSingleUserById(id);

        if (user == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, INVALID_USER_ID), HttpStatus.BAD_REQUEST);
        }
        Role role = new Role();
        role.setObjectId(roleDTO.getRoleId());
        user.setRole(role);

        iudAnswer = updateUser(id, user);

        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.OK);
        } else {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
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
		IUDAnswer answer = updateUser(id, user);
		if (answer != null && answer.isSuccessful()) {
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
		IUDAnswer answer = updateUser(id, user);
		return checkUpdateForAdmin(answer, request, sessionUser, id);
	}

	//Delete user method
	@RequestMapping(value = "delete/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<IUDAnswer> deleteUser(@PathVariable("id") Integer id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("USER");
		IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
		if (!iudAnswer.isSuccessful()) {
		    return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }

        try {
            iudAnswer = userService.deleteUser(id);
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(RESTORED_USER_NOT_EXISTS, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, RESTORED_USER_NOT_EXISTS, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_RESTORED_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_RESTORED_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

		HttpStatus status;
		if (iudAnswer != null && iudAnswer.isSuccessful()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.NOT_FOUND;
		}

		ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
		return responseEntity;
	}

    @RequestMapping(value = "restore/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> restoreUser(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }

        try {
            iudAnswer = userService.restoreUser(id);
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(RESTORED_USER_NOT_EXISTS, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, RESTORED_USER_NOT_EXISTS, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_RESTORED_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_RESTORED_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

	//returned user-client from session
	@RequestMapping(value = "/myself")
	public ResponseEntity<User> getUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("USER");
		if (user == null) {
		    return new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
        }
		Integer userId = user.getObjectId();
		HttpStatus status;
		user = userService.getSingleUserById(userId);
		user.setPassword(null);
		status = HttpStatus.OK;

		return new ResponseEntity<User>(user, status);
	}

	private IUDAnswer updateUser(Integer id, User user) {
        try {
            return userService.updateUser(id, user);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
        } catch (DuplicateEmailException e) {
            LOGGER.warn(DUPLICATE_EMAIL, e);
            return new IUDAnswer(id, false, DUPLICATE_EMAIL, e.getMessage());
        }
    }

	private IUDAnswer registerUser(User user) {
        try {
            return userService.insertUser(user);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        } catch (DuplicateEmailException e) {
            LOGGER.warn(DUPLICATE_EMAIL, e);
            return new IUDAnswer(false, DUPLICATE_EMAIL, e.getMessage());
        }
    }

    private IUDAnswer changePassword(Integer id, User user) {
        try {
            return userService.updateUserPassword(id, user);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
        } catch (DuplicateEmailException e) {
            LOGGER.warn(DUPLICATE_EMAIL, e);
            return new IUDAnswer(id, false, DUPLICATE_EMAIL, e.getMessage());
        }
    }

	private ResponseEntity<IUDAnswer> checkUpdateForAdmin(IUDAnswer answer, HttpServletRequest request, User sessionUser, Integer id) {
        if (answer.isSuccessful() && sessionUser.getObjectId() == id.intValue()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(id));
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        } else if (answer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        } else {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
        }
    }
}
