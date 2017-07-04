package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

@Repository
public class PhoneDAOImpl extends AbstractDAO implements PhoneDAO{
    private static final Logger LOGGER = Logger.getLogger(PhoneDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Phone getPhone(Integer id) {
        if (id == null) {
            return null;
        }

        return (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(id);
    }

    @Override
    public List<Phone> getAllPhones() {
        return manager.createReactEAV(Phone.class).getEntityCollection();
    }

    /**
     * @throws IllegalArgumentException if phone has fields which are empty or null.
     * @param phone
     * @return objectId of new phone
     */
    @Override
    public Integer insertPhone(Phone phone) {
        if (phone == null) {
            return null;
        }
        Integer objectId = nextObjectId();
        jdbcTemplate.update(INSERT_OBJECT, objectId, phone.getUserId(), 9, null, null);
        insertPhoneNumber(objectId, phone);
        return objectId;
    }

    /**
     * @throws IllegalArgumentException if oldPhone or newPhone has fields which are empty or null.
     * @param newPhone
     * @param oldPhone
     * @return objectId of newPhone
     */
    @Override
    public Integer updatePhone(Phone newPhone, Phone oldPhone) {
        if (oldPhone == null || newPhone == null) {
            return null;
        }
        updatePhoneNumber(newPhone, oldPhone);
        return newPhone.getObjectId();
    }

    /**
     * @throws WrongEntityIdException if id for delete operation belong another entity.
     * @throws DeletedObjectNotExistsException if id for delete operation belong object which does not exists.
     * @throws ReferenceBreakException if id belong entity, which have references on self.
     * @param id
     */
    @Override
    public void deletePhone(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Phone phone = null;
        try{
            phone = getPhone(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == phone){
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertPhoneNumber(Integer objectId, Phone phone) {
        if (phone.getPhoneNumber() == null || phone.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 38, objectId, phone.getPhoneNumber(), null);
        }
    }

    private void updatePhoneNumber(Phone newPhone, Phone oldPhone) {
        if (oldPhone.getPhoneNumber() != null && newPhone.getPhoneNumber() != null && !newPhone.getPhoneNumber().isEmpty()) {
            if (!oldPhone.getPhoneNumber().equals(newPhone.getPhoneNumber())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newPhone.getPhoneNumber(), null, newPhone.getObjectId(), 38);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
