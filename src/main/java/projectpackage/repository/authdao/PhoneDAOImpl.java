package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.daoexceptions.WrongIdException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

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
    public int insertPhone(Phone phone) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, phone.getUserId(), 9, null, null);
            jdbcTemplate.update(insertAttribute, 38, objectId, phone.getPhoneNumber(), null);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException {
        try {
            if (!oldPhone.getPhoneNumber().equals(newPhone.getPhoneNumber())) {
                jdbcTemplate.update(updateAttribute, newPhone.getPhoneNumber(), null, newPhone.getObjectId(), 38);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deletePhone(int id) throws ReferenceBreakException, WrongEntityIdException, WrongIdException {
        Phone phone = null;
        try{
            phone = getPhone(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == phone) throw new WrongIdException(this);

        deleteSingleEntityById(id);
    }
}
