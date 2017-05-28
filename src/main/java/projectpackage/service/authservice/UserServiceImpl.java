package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.authdao.UserDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

@Log4j
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    UserDAO userDAO;

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users == null) LOGGER.info("Returned NULL!!!");
        return users;
    }

    @Override
    public User getSingleUserById(int id) {
        User user = userDAO.getUser(id);
        if (user == null) LOGGER.info("Returned NULL!!!");
        return user;
    }

    @Override
    public IUDAnswer deleteUser(int id) {
        try {
            userDAO.deleteUser(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertUser(User user) {
        Integer userId = null;
        try {
            userId = userDAO.insertUser(user);
            LOGGER.info("Get from DB userId = " + userId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            new IUDAnswer(userId,false, "transactionInterrupt");
        }
        return new IUDAnswer(userId,true);
    }

    @Override
    public IUDAnswer updateUser(int id, User newUser) {
        try {
            newUser.setObjectId(id);
            User oldUser = userDAO.getUser(id);
            userDAO.updateUser(newUser, oldUser);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
