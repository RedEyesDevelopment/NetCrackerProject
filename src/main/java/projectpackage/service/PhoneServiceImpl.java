package projectpackage.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.repository.DeleteDAO;
import projectpackage.repository.PhoneDAO;
import projectpackage.repository.reacdao.ReactEAVManager;
import projectpackage.repository.reacdao.exceptions.ResultEntityNullException;

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
    DeleteDAO deleteDAO;

    @Autowired
    ReactEAVManager manager;

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
    public int deletePhoneById(int id) {
        return deleteDAO.deleteSingleEntityById(id);
    }

    @Override
    public void insertPhone(Phone phone) {
        phoneDAO.insertPhone(phone);
    }

    @Override
    public boolean updatePhone(Phone newPhone, int oldPhoneId) {
        try {
            Phone phone = (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(oldPhoneId);
            phoneDAO.updatePhone(newPhone, phone);
        } catch (ResultEntityNullException e) {
            return false;
        }
        return true;
    }
}
