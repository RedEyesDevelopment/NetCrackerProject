package projectpackage.repository.authdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.model.auth.Phone;

@Repository
public class PhoneDAOImpl extends AbstractDAO implements PhoneDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insertPhone(Phone phone) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, phone.getUserId(), 9, null, null);
            jdbcTemplate.update(insertAttributes, 38, objectId, phone.getPhoneNumber(), null);
        } catch (NullPointerException e) {
            throw new TransactionException(phone);
        }
        return objectId;
    }

    @Override
    public void updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException {
        try {
            if (!oldPhone.getPhoneNumber().equals(newPhone.getPhoneNumber())) {
                jdbcTemplate.update(updateAttributes, newPhone.getPhoneNumber(), null, newPhone.getObjectId(), 38);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newPhone);
        }
    }

    @Override
    public int deletePhone(int id) {
        return deleteSingleEntityById(id);
    }
}
