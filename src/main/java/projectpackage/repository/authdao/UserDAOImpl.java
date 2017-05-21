package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

@Repository
public class UserDAOImpl extends AbstractDAO implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public User getUser(Integer id) {
        if (id == null) return null;
        try {
            return (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeFetch()
                    .fetchInnerReference(Role.class, "RoleToUser").closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeFetch()
                    .fetchInnerReference(Role.class, "RoleToUser").closeAllFetches()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertUser(User user) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 3, null, null);                      //3 = User

            jdbcTemplate.update(insertAttribute, 15, objectId, user.getEmail(), null);             //email
            jdbcTemplate.update(insertAttribute, 16, objectId, user.getPassword(), null);          //password
            jdbcTemplate.update(insertAttribute, 17, objectId, user.getFirstName(), null);         //first_name
            jdbcTemplate.update(insertAttribute, 18, objectId, user.getLastName(), null);          //last_name
            jdbcTemplate.update(insertAttribute, 19, objectId, user.getAdditionalInfo(), null);    //additional_info
            if (user.getEnabled()) {
                jdbcTemplate.update(insertAttribute, 3, objectId, "true", null);
            } else {
                jdbcTemplate.update(insertAttribute, 3, objectId, "false", null);
            }

            jdbcTemplate.update(insertObjReference, 20, objectId, user.getRole().getObjectId());    //hasRole
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
        return objectId;
    }

    /**
     * Метод получает нового юзера и старого, затем сравнивает их поля и выполняет запросы по шаблону
     */
    @Override
    public void updateUser(User newUser, User oldUser) throws TransactionException {
        try {
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                jdbcTemplate.update(updateAttribute, newUser.getEmail(), null, newUser.getObjectId(), 15);
            }
            if (!oldUser.getPassword().equals(newUser.getPassword())) {
                jdbcTemplate.update(updateAttribute, newUser.getPassword(), null, newUser.getObjectId(), 16);
            }
            if (!oldUser.getFirstName().equals(newUser.getFirstName())) {
                jdbcTemplate.update(updateAttribute, newUser.getFirstName(), null, newUser.getObjectId(), 17);
            }
            if (!oldUser.getLastName().equals(newUser.getLastName())) {
                jdbcTemplate.update(updateAttribute, newUser.getLastName(), null, newUser.getObjectId(), 18);
            }
            if (!oldUser.getAdditionalInfo().equals(newUser.getAdditionalInfo())) {
                jdbcTemplate.update(updateAttribute, newUser.getAdditionalInfo(), null, newUser.getObjectId(), 19);
            }
            if (!oldUser.getEnabled().equals(newUser.getEnabled())) {
                if (newUser.getEnabled()) {
                    jdbcTemplate.update(updateAttribute, "true", null, newUser.getObjectId(), 3);
                } else {
                    jdbcTemplate.update(updateAttribute, "false", null, newUser.getObjectId(), 3);
                }
            }
            if (oldUser.getRole().getObjectId() != newUser.getRole().getObjectId()) {
                jdbcTemplate.update(updateReference, newUser.getRole().getObjectId(), newUser.getObjectId(), 20);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
    }

    @Override
    public void deleteUser(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }

}
