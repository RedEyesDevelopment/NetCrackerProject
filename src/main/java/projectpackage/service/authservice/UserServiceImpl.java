package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.authdao.UserDAO;
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

	@Transactional(readOnly = true)
	@Override
	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
		if (users == null) {
			LOGGER.info("Returned NULL!!!");
		}
		return users;
	}

	@Transactional(readOnly = true)
	@Override
	public User getSingleUserById(Integer id) {
		User user = userDAO.getUser(id);
		if (user == null) {
			LOGGER.info("Returned NULL!!!");
		}
		return user;
	}

	@Transactional(readOnly = true)
	@Override
	public User getSingleUserByUsername(String username) {
		if (null == username){
			return null;
		}
		return userDAO.getUserByUsername(username);
	}

	@Transactional
	@Override
	public IUDAnswer deleteUser(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        userDAO.deleteUser(id);

        return new IUDAnswer(id, true);
	}

	@Transactional
	@Override
	public IUDAnswer restoreUser(Integer id) {
		if (id == null) {
			return new IUDAnswer(false, NULL_ID);
		}
        userDAO.restoreUser(id);

		return new IUDAnswer(id, true);
	}

	@Transactional
	@Override
	public IUDAnswer insertUser(User user) {
		if (user == null) {
			return new IUDAnswer(false, NULL_ENTITY);
		}
		if (user.getPhones() == null) {
			return new IUDAnswer(false, NULL_ENTITY);
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

		Integer userId = userDAO.insertUser(user);

		for (Phone phone : user.getPhones()) {
			phone.setUserId(userId);
			phone.setObjectId(phoneDAO.insertPhone(phone));
		}

		return new IUDAnswer(userId, true);
	}

	@Transactional
	@Override
	public IUDAnswer updateUser(Integer id, User newUser) {
	    if (newUser == null) {
            return new IUDAnswer(false, NULL_ENTITY);
		}
        if (id == null) {
	    	return new IUDAnswer(false, NULL_ID);
		}

        newUser.setObjectId(id);
        User oldUser = userDAO.getUser(id);
        userDAO.updateUser(newUser, oldUser);

		return new IUDAnswer(id, true);
	}

	@Transactional
	@Override
	public IUDAnswer updateUserPassword(Integer id, User newUser) {
		if (newUser == null) {
			return new IUDAnswer(false, NULL_ENTITY);
		}
		if (id == null) {
			return new IUDAnswer(false, NULL_ID);
		}
		if (!securityService.cryptUserPass(newUser)) {
			return new IUDAnswer(false, FAIL_CRYPT_PASSWORD);
		}
        newUser.setObjectId(id);
        User oldUser = userDAO.getUser(id);
        userDAO.updateUserPassword(newUser, oldUser);

		return new IUDAnswer(id, true);
	}

}
