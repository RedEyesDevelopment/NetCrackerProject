package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
@Log4j
@Service
public class PhoneServiceImpl implements PhoneService{
    private static final Logger LOGGER = Logger.getLogger(PhoneServiceImpl.class);

    @Autowired
    PhoneDAO phoneDAO;

    @Override
    public List<Phone> getAllPhones() {
        List<Phone> phones = phoneDAO.getAllPhones();
        if (phones == null) LOGGER.info("Returned NULL!!!");
        return phones;
    }

    @Override
    public List<Phone> getAllPhonesByUser(User user) {
        return null;
    }

    @Override
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Phone getSinglePhoneById(int id) {
        Phone phone = phoneDAO.getPhone(id);
        if (phone == null) LOGGER.info("Returned NULL!!!");
        return phone;
    }

    @Override
    public boolean deletePhone(int id) {
        int count = phoneDAO.deletePhone(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertPhone(Phone phone) {
        try {
            int phoneId = phoneDAO.insertPhone(phone);
            LOGGER.info("Get from DB phoneId = " + phoneId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePhone(int id, Phone newPhone) {
        try {
            newPhone.setObjectId(id);
            Phone oldPhone = phoneDAO.getPhone(id);
            phoneDAO.updatePhone(newPhone, oldPhone);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
