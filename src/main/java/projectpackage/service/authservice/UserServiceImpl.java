package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.authdao.UserDAO;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

@Log4j
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    PhoneDAO phoneDAO;

    @Override
    public List<User> getUsersByRole(Role role) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users == null) LOGGER.info("Returned NULL!!!");
        return users;
    }

    @Override
    public List<User> getAllUsers(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public User getSingleUserById(int id) {
        User user = userDAO.getUser(id);
        if (user == null) LOGGER.info("Returned NULL!!!");
        return user;
    }

    @Override
    public boolean deleteUser(int id) {
        User user = userDAO.getUser(id);
        int count = 0;
        if (null != user.getPhones()) {
            for (Phone phone : user.getPhones()) {
                count = count + phoneDAO.deletePhone(phone.getObjectId());
            }
        }
        count = count + userDAO.deleteUser(id);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertUser(User user) {
        try {
            int userId = userDAO.insertUser(user);
            LOGGER.info("Get from DB phoneId = " + userId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateUser(int id, User newUser) {
        try {
            newUser.setObjectId(id);
            User oldUser = userDAO.getUser(id);
            userDAO.updateUser(newUser, oldUser);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
