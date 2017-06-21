package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.*;

import java.util.List;

@Repository
public class UserDAOImpl extends AbstractDAO implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    private void checkEmailForDuplicate(String email) throws DuplicateEmailException {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("User_tools").withProcedureName("check_email_for_dupl");
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("in_email", email);
        try {
            call.executeFunction(Object.class, in);
        } catch (UncategorizedSQLException e) {
            if (e.getSQLException().getErrorCode() == 20002) {
                throw new DuplicateEmailException(email);
            } else {
                throw e;
            }
        }
    }

    @Override
    public User getUser(Integer id) {
        if (id == null) return null;
        try {
            return (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches()
                    .fetchRootReference(Role.class, "RoleToUser").closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches()
                    .fetchRootReference(Role.class, "RoleToUser").closeAllFetches()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public Integer insertUser(User user) throws TransactionException, DuplicateEmailException {
        if (user == null) return null;
        Integer objectId = nextObjectId();
        checkEmailForDuplicate(user.getEmail());
        try {
            jdbcTemplate.update(insertObject, objectId, null, 3, null, null);
            insertEmail(objectId, user);
            insertPassword(objectId, user);
            insertFirstName(objectId, user);
            insertLastName(objectId, user);
            insertAdditionalInfo(objectId, user);
            insertEnabled(objectId, user);
            insertRole(objectId, user);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    /**
     * Метод получает нового юзера и старого, затем сравнивает их поля и выполняет запросы по шаблону
     */
    @Override
    public Integer updateUser(User newUser, User oldUser) throws TransactionException {
        if (newUser == null || oldUser == null) return null;
        try {
            updateEmail(newUser, oldUser);
            updatePassword(newUser, oldUser);
            updateFirstName(newUser, oldUser);
            updateLastName(newUser, oldUser);
            updateAdditionalInfo(newUser, oldUser);
            updateEnabled(newUser, oldUser);
            updateRole(newUser, oldUser);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newUser.getObjectId();
    }

    @Override
    public void deleteUser(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        User user = null;
        try {
            user = getUser(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == user) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }

    private void insertEmail(Integer objectId, User user) {
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            jdbcTemplate.update(insertAttribute, 15, objectId, user.getEmail(), null);
        } else {
            jdbcTemplate.update(insertAttribute, 15, objectId, null, null);
        }
    }

    private void insertPassword(Integer objectId, User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            jdbcTemplate.update(insertAttribute, 16, objectId, user.getPassword(), null);
        } else {
            jdbcTemplate.update(insertAttribute, 16, objectId, null, null);
        }
    }

    private void insertFirstName(Integer objectId, User user) {
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            jdbcTemplate.update(insertAttribute, 17, objectId, user.getFirstName(), null);
        } else {
            jdbcTemplate.update(insertAttribute, 17, objectId, null, null);
        }
    }

    private void insertLastName(Integer objectId, User user) {
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            jdbcTemplate.update(insertAttribute, 18, objectId, user.getLastName(), null);
        } else {
            jdbcTemplate.update(insertAttribute, 18, objectId, null, null);
        }
    }

    private void insertAdditionalInfo(Integer objectId, User user) {
        if (user.getAdditionalInfo() != null && !user.getAdditionalInfo().isEmpty()) {
            jdbcTemplate.update(insertAttribute, 19, objectId, user.getAdditionalInfo(), null);
        } else {
            jdbcTemplate.update(insertAttribute, 19, objectId, null, null);
        }
    }

    private void insertEnabled(Integer objectId, User user) {
        if (user.getEnabled() != null && user.getEnabled()) {
            jdbcTemplate.update(insertAttribute, 3, objectId, "true", null);
        } else {
            jdbcTemplate.update(insertAttribute, 3, objectId, "false", null);
        }
    }

    private void insertRole(Integer objectId, User user) {
        if (user.getRole() != null) {
            jdbcTemplate.update(insertObjReference, 20, objectId, user.getRole().getObjectId());
        } else {
            jdbcTemplate.update(insertObjReference, 20, objectId, 3);
        }
    }

    private void updateEmail(User newUser, User oldUser) {
        if (oldUser.getEmail() != null && newUser.getEmail() != null
                && !oldUser.getEmail().isEmpty() && !newUser.getEmail().isEmpty()) {
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                jdbcTemplate.update(updateAttribute, newUser.getEmail(), null, newUser.getObjectId(), 15);
            }
        } else {
            jdbcTemplate.update(updateAttribute, null, null, newUser.getObjectId(), 15);
        }
    }

    private void updatePassword(User newUser, User oldUser) {
        if (oldUser.getPassword() != null && newUser.getPassword() != null && !oldUser.getPassword().isEmpty()
                && !newUser.getPassword().isEmpty()) {
            if (!oldUser.getPassword().equals(newUser.getPassword())) {
                jdbcTemplate.update(updateAttribute, newUser.getPassword(), null, newUser.getObjectId(), 16);
            }
        } else {
            jdbcTemplate.update(updateAttribute, null, null, newUser.getObjectId(), 16);
        }
    }

    private void updateFirstName(User oldUser, User newUser) {
        if (oldUser.getFirstName() != null && newUser.getLastName() != null && !oldUser.getFirstName().isEmpty()
                && !newUser.getFirstName().isEmpty()) {
            if (!oldUser.getFirstName().equals(newUser.getFirstName())) {
                jdbcTemplate.update(updateAttribute, newUser.getFirstName(), null, newUser.getObjectId(), 17);
            }
        } else {
            jdbcTemplate.update(updateAttribute, null, null, newUser.getObjectId(), 17);
        }
    }

    private void updateLastName(User newUser, User oldUser) {
        if (oldUser.getLastName() != null && newUser.getLastName() != null && !oldUser.getLastName().isEmpty()
                && !newUser.getLastName().isEmpty()) {
            if (!oldUser.getLastName().equals(newUser.getLastName())) {
                jdbcTemplate.update(updateAttribute, newUser.getLastName(), null, newUser.getObjectId(), 18);
            }
        } else {
            jdbcTemplate.update(updateAttribute, null, null, newUser.getObjectId(), 18);
        }
    }

    private void updateAdditionalInfo(User newUser, User oldUser) {
        if (oldUser.getAdditionalInfo() != null && newUser.getAdditionalInfo() != null && !oldUser.getAdditionalInfo().isEmpty()
                && !newUser.getAdditionalInfo().isEmpty()) {
            if (!oldUser.getAdditionalInfo().equals(newUser.getAdditionalInfo())) {
                jdbcTemplate.update(updateAttribute, newUser.getAdditionalInfo(), null, newUser.getObjectId(), 19);
            }
        } else {
            jdbcTemplate.update(updateAttribute, null, null, newUser.getObjectId(), 19);
        }
    }

    private void updateEnabled(User newUser, User oldUser) {
        if (oldUser.getEnabled() != null && newUser.getEnabled() != null) {
            if (!oldUser.getEnabled().equals(newUser.getEnabled())) {
                if (newUser.getEnabled()) {
                    jdbcTemplate.update(updateAttribute, "true", null, newUser.getObjectId(), 3);
                } else {
                    jdbcTemplate.update(updateAttribute, "false", null, newUser.getObjectId(), 3);
                }
            }
        } else {
            jdbcTemplate.update(updateAttribute, "false", null, newUser.getObjectId(), 3);
        }
    }

    private void updateRole(User newUser, User oldUser) {
        if (oldUser.getRole() != null && newUser.getRole() != null) {
            if (oldUser.getRole().getObjectId() != newUser.getRole().getObjectId()) {
                jdbcTemplate.update(updateReference, newUser.getRole().getObjectId(), newUser.getObjectId(), 20);
            }
        } else {
            jdbcTemplate.update(updateReference, 3, newUser.getObjectId(), 20);
        }
    }
}
