package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.authdao.UserDAO;
import projectpackage.repository.support.daoexceptions.*;
import projectpackage.service.phoneregex.PhoneRegexService;

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
	PhoneRegexService phoneRegexService;

	@Override
	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
		if (users == null) LOGGER.info("Returned NULL!!!");
		return users;
	}

	@Override
	public User getSingleUserById(Integer id) {
		User user = userDAO.getUser(id);
		if (user == null) LOGGER.info("Returned NULL!!!");
		return user;
	}

	@Override
	public IUDAnswer deleteUser(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            userDAO.deleteUser(id);
        } catch (ReferenceBreakException e) {
            return userDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            userDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            userDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            userDAO.rollback(id, NULL_ID, e);
        }
        userDAO.commit();
        return new IUDAnswer(id, true);
	}

	@Override
	public IUDAnswer insertUser(User user) {
		Phone phone = user.getPhones().iterator().next();
		boolean isValid = phoneRegexService.match(phone.getPhoneNumber());
		if (!isValid) return new IUDAnswer(false, WRONG_PHONE_NUMBER);

		Integer userId = null;
		try {
			userId = userDAO.insertUser(user);
		} catch (IllegalArgumentException e) {
			userDAO.rollback(WRONG_FIELD, e);
		} catch (DuplicateEmailException e) {
			userDAO.rollback(DUPLICATE_EMAIL, e);
		}
		phone.setUserId(userId);
		try {
			phone.setObjectId(phoneDAO.insertPhone(phone));
		} catch (IllegalArgumentException e) {
		    phoneDAO.rollback(WRONG_FIELD, e);
		}
		phoneDAO.commit();
		return new IUDAnswer(userId, true);
	}

	@Override
	public IUDAnswer updateUser(Integer id, User newUser) {
		try {
			newUser.setObjectId(id);
			User oldUser = userDAO.getUser(id);
			userDAO.updateUser(newUser, oldUser);
		} catch (IllegalArgumentException e) {
		    userDAO.rollback(WRONG_FIELD, e);
		}
		userDAO.commit();
		return new IUDAnswer(id, true);
	}


}
