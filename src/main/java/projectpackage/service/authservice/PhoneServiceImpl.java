package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
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
            log.warn("getAllPhones method returned null list", e);
        }
        return phones;
    }

    @Override
    public Phone getSinglePhoneById(int id) {
        Phone phone = null;
        try {
            phone = (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            log.warn("getSinglePhoneById method returned null list", e);
        }
        return phone;
    }

    @Override
    public boolean deletePhone(int id) {
        int count = phoneDAO.deletePhone(id);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertPhone(Phone phone) {
        try {
            phoneDAO.insertPhone(phone);
        } catch (TransactionException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePhone(int id, Phone newPhone) {
        try {
            newPhone.setObjectId(id);
            Phone phone = (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(newPhone.getObjectId());
            phoneDAO.updatePhone(newPhone, phone);
        } catch (ResultEntityNullException e) {
            return false;
        } catch (TransactionException e) {
            return false;
        }
        return true;
    }
}
