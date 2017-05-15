package projectpackage.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.DeleteDAO;
import projectpackage.repository.UserDAO;
import projectpackage.repository.reacdao.ReactEAVManager;
import projectpackage.repository.reacdao.exceptions.ResultEntityNullException;

import java.util.List;

@Log4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    DeleteDAO deleteDAO;

    @Autowired
    ReactEAVManager manager;

    @Override
    public List<User> getAllUsers(String orderingParameter, boolean ascend) {
        List<User> list = null;
        try {
             list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getEntityCollectionOrderByParameter(orderingParameter, ascend);
        } catch (ResultEntityNullException e) {
            log.warn("getAllUsers method returned null list", e);
        }
        return list;
    }

    @Override
    public User getSingleUserById(int id) {
        User user=null;
        try {
            user = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            log.warn("getSingleUserById method returned null list", e);
        }
        return user;
    }

    @Override
    public int deleteUserById(int id) {
        return deleteDAO.deleteSingleEntityById(id);
    }

    @Override
    public void insertUser(User user) {
        userDAO.insertUser(user);
    }

    @Override
    public boolean updateUser(User newUser, int oldUserId) {
        try {
            User oldUser = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(oldUserId);
            userDAO.updateUser(newUser,oldUser);
        } catch (ResultEntityNullException e) {
            return false;
        }
        return true;
    }
}
