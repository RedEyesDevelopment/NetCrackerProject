package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.authdao.UserDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.DuplicateEmailException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.regex.RegexService;
import projectpackage.service.securityservice.SecurityService;

import java.util.List;

@Log4j
@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	UserDAO userDAO;

	@Autowired
	PhoneDAO phoneDAO;

	@Autowired
	RegexService regexService;

	@Autowired
	SecurityService securityService;

	@Override
	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
		if (users == null) {
			LOGGER.info("Returned NULL!!!");
		}
		return users;
	}

	@Override
	public User getSingleUserById(Integer id) {
		User user = userDAO.getUser(id);
		if (user == null) {
			LOGGER.info("Returned NULL!!!");
		}
		return user;
	}

	@Override
	public User getSingleUserByUsername(String username) {
		if (null == username){
			return null;
		}
		return userDAO.getUserByUsername(username);
	}

	@Override
	public IUDAnswer deleteUser(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            userDAO.deleteUser(id);
		} catch (DeletedObjectNotExistsException e) {
			LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
			return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
		} catch (WrongEntityIdException e) {
			LOGGER.warn(WRONG_DELETED_ID, e);
			return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.warn(NULL_ID, e);
			return new IUDAnswer(id, false, NULL_ID, e.getMessage());
		}
        userDAO.commit();
        return new IUDAnswer(id, true);
	}

	@Override
	public IUDAnswer restoreUser(Integer id) {
		if (id == null) {
			return new IUDAnswer(false, NULL_ID);
		}
		try {
			userDAO.restoreUser(id);
		} catch (DeletedObjectNotExistsException e) {
			LOGGER.warn(RESTORED_USER_NOT_EXISTS, e);
			return new IUDAnswer(id, false, RESTORED_USER_NOT_EXISTS, e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.warn(NULL_ID, e);
			return new IUDAnswer(id, false, NULL_ID, e.getMessage());
		} catch (WrongEntityIdException e) {
			LOGGER.warn(WRONG_RESTORED_ID, e);
			return new IUDAnswer(id, false, WRONG_RESTORED_ID, e.getMessage());
		}
		userDAO.commit();
		return new IUDAnswer(id, true);
	}

	@Override
	public IUDAnswer insertUser(User user) {
		if (user == null) {
			return null;
		}
		if (user.getPhones() == null) {
			return null;
		}
        for (Phone phone : user.getPhones()) {
            boolean isValid = regexService.isValidPhone(phone.getPhoneNumber());
            if (!isValid) {
            	return new IUDAnswer(false, WRONG_PHONE_NUMBER);
			}
        }
        if (!securityService.cryptUserPass(user)){
        	return new IUDAnswer(false, INVALID_PASSWORD);
		}

		Integer userId = null;
		try {
			userId = userDAO.insertUser(user);
		} catch (IllegalArgumentException e) {
			LOGGER.warn(WRONG_FIELD, e);
			return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
		} catch (DuplicateEmailException e) {
			LOGGER.warn(DUPLICATE_EMAIL, e);
			return new IUDAnswer(false, DUPLICATE_EMAIL, e.getMessage());
		}

        try {
            for (Phone phone : user.getPhones()) {
                phone.setUserId(userId);
                phone.setObjectId(phoneDAO.insertPhone(phone));
            }
        } catch (IllegalArgumentException e) {
			LOGGER.warn(WRONG_FIELD, e);
			return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
		phoneDAO.commit();
		return new IUDAnswer(userId, true);
	}

	@Override
	public IUDAnswer updateUser(Integer id, User newUser) {
	    if (newUser == null) {
	    	return null;
		}
        if (id == null) {
	    	return new IUDAnswer(false, NULL_ID);
		}
		try {
			newUser.setObjectId(id);
			User oldUser = userDAO.getUser(id);
			userDAO.updateUser(newUser, oldUser);
		} catch (IllegalArgumentException e) {
			LOGGER.warn(WRONG_FIELD, e);
			return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
		} catch (DuplicateEmailException e) {
			LOGGER.warn(DUPLICATE_EMAIL, e);
			return new IUDAnswer(id, false, DUPLICATE_EMAIL, e.getMessage());
		}
		userDAO.commit();
		return new IUDAnswer(id, true);
	}

	@Override
	public IUDAnswer updateUserPassword(Integer id, User newUser) {
		if (newUser == null) {
			return null;
		}
		if (id == null) {
			return new IUDAnswer(false, NULL_ID);
		}
		if (!securityService.cryptUserPass(newUser)) {
			return new IUDAnswer(false, FAIL_CRYPT_PASSWORD);
		}
		try {
			newUser.setObjectId(id);
			User oldUser = userDAO.getUser(id);
			userDAO.updateUserPassword(newUser, oldUser);
		} catch (IllegalArgumentException e) {
			LOGGER.warn(WRONG_FIELD, e);
			return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
		} catch (DuplicateEmailException e) {
			LOGGER.warn(DUPLICATE_EMAIL, e);
			return new IUDAnswer(id, false, DUPLICATE_EMAIL, e.getMessage());
		}
		userDAO.commit();
		return new IUDAnswer(id, true);
	}

}
