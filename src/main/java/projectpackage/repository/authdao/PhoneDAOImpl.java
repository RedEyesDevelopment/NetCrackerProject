package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

@Repository
public class PhoneDAOImpl extends AbstractDAO implements PhoneDAO{
    private static final Logger LOGGER = Logger.getLogger(PhoneDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Phone getPhone(Integer id) {
        if (id == null) return null;
        try {
            return (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Phone> getAllPhones() {
        try {
            return manager.createReactEAV(Phone.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public Integer insertPhone(Phone phone) throws TransactionException {
        if (phone == null) return null;
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, phone.getUserId(), 9, null, null);
            insertPhoneNumber(objectId, phone);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public Integer updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException {
        if (oldPhone == null || newPhone == null) return null;
        try {
            updatePhoneNumber(newPhone, oldPhone);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newPhone.getObjectId();
    }

    @Override
    public void deletePhone(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Phone phone = null;
        try{
            phone = getPhone(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == phone) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }

    private void insertPhoneNumber(Integer objectId, Phone phone) {
        if (phone.getPhoneNumber() == null || phone.getPhoneNumber().isEmpty()) {
            jdbcTemplate.update(insertAttribute, 38, objectId, null, null);
        } else {
            jdbcTemplate.update(insertAttribute, 38, objectId, phone.getPhoneNumber(), null);
        }
    }

    private void updatePhoneNumber(Phone newPhone, Phone oldPhone) {
        if (oldPhone != null && newPhone != null && !oldPhone.getPhoneNumber().isEmpty()
                && !newPhone.getPhoneNumber().isEmpty()) {
            if (!oldPhone.getPhoneNumber().equals(newPhone.getPhoneNumber())) {
                jdbcTemplate.update(updateAttribute, newPhone.getPhoneNumber(), null, newPhone.getObjectId(), 38);
            }
        } else {
            jdbcTemplate.update(updateAttribute, null, null, newPhone.getObjectId(), 38);
        }
    }
}
