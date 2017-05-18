package projectpackage.service.authservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.authdao.UserDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    PhoneDAO phoneDAO;

    @Autowired
    ReactEAVManager manager;

    @Override
    public List<User> getUsersByRole(Role role) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public List<User> getAllUsers(String orderingParameter, boolean ascend) {
//        List<User> list = null;
//        try {
//             list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getEntityCollectionOrderByParameter(orderingParameter, ascend);
//        } catch (ResultEntityNullException e) {
//            log.warn("getAllUsers method returned null list", e);
//        }
//        return list;
        log.info("method getAllUsers!");
        return null;
    }

    @Override
    public User getSingleUserById(int id) {
        User user=null;
        try {
            user = (User) manager.createReactEAV(User.class).fetchChildEntityCollection(Phone.class).closeFetch().fetchReferenceEntityCollectionForInnerObject(Role.class, "RoleToUser").closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            log.warn("getSingleUserById method returned null list", e);
        }
        return user;
    }

    @Override
    public boolean deleteUser(int id) {
        return false;
//        User user = null;
//        try {
//            user = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(id);
//        } catch (ResultEntityNullException e) {
//            return false;
//        }
//        int count = 0;
//        if (null != user.getPhones()) {
//            for (Phone phone : user.getPhones()) {
//                count = count + phoneDAO.deletePhone(phone.getObjectId());
//            }
//        }
//        count = count + userDAO.deleteUser(id);
//        if (count == 0) return false;
//        else return true;
    }

    @Override
    public boolean insertUser(User user) {
        try {
            System.out.println(userDAO.insertUser(user));
        } catch (TransactionException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateUser(int id, User newUser) {
        return false;
//        try {
//            newUser.setObjectId(id);
//            User oldUser = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(newUser.getObjectId());
//            userDAO.updateUser(newUser,oldUser);
//        } catch (ResultEntityNullException e) {
//            return false;
//        } catch (TransactionException e) {
//            return false;
//        }
//        return true;
    }
}
