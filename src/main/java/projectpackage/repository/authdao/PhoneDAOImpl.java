package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;
import java.util.Optional;

@Repository
public class PhoneDAOImpl extends AbstractDAO implements PhoneDAO{
    private static final Logger LOGGER = Logger.getLogger(PhoneDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Phone> getPhone(Integer id) {
        if (id == null) return null;
            return manager.createReactEAV(Phone.class).getSingleEntityWithId(id);
    }

    @Override
    public List<Optional<ReactEntityWithId>> getAllPhones() {
            return manager.createReactEAV(Phone.class).getEntityCollection();
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
    public void deletePhone(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Optional<Phone> phone = getPhone(id);
        if (null == phone) throw new DeletedObjectNotExistsException(this);
        deleteSingleEntityById(id);
    }
}
