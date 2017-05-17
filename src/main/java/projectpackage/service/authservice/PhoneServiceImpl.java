package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

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

    @Autowired
    ReactEAVManager manager;

    @Override
    public List<Phone> getAllPhones() {
        return null;
    }

    @Override
    public List<Phone> getAllPhonesByUser(User user) {
        return null;
    }

    @Override
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend) {
        List<Phone> phones = null;
        try {
            phones = (List<Phone>) manager.createReactEAV(Phone.class).getEntityCollectionOrderByParameter(orderingParameter, ascend);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("getAllPhones method returned null list", e);
        }
        return phones;
    }

    @Override
    public Phone getSinglePhoneById(int id) {
        Phone phone = null;
        try {
            phone = (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("getSinglePhoneById method returned null list", e);
        }
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
            Phone oldPhone = (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(newPhone.getObjectId());
            phoneDAO.updatePhone(newPhone, oldPhone);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
            return false;
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
