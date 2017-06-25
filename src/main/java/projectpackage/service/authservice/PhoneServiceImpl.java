package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.phoneregex.PhoneRegexService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
@Log4j
@Service
public class PhoneServiceImpl implements PhoneService{
    private static final Logger LOGGER = Logger.getLogger(PhoneServiceImpl.class);

    @Autowired
    PhoneRegexService phoneRegexService;

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
        List<Phone> answer = new ArrayList<>();
        try {
            String email = user.getEmail();
            List<Phone> allUser = getAllPhones();
            for (Phone phone : allUser) {
                if (user.getPhones().contains(phone)) {
                    answer.add(phone);
                }
            }
        } catch (ResultEntityNullException e) {
            LOGGER.warn("Returned NULL!!!");
        }
        return answer;
    }

    @Override
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Phone getSinglePhoneById(Integer id) {
        Phone phone = phoneDAO.getPhone(id);
        if (phone == null) LOGGER.info("Returned NULL!!!");
        return phone;
    }

    @Override
    public IUDAnswer deletePhone(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            phoneDAO.deletePhone(id);
        } catch (ReferenceBreakException e) {
            return phoneDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return phoneDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return phoneDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return phoneDAO.rollback(id, NULL_ID, e);
        }
        phoneDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertPhone(Phone phone) {
        if (phone == null) return null;
        boolean isValid = phoneRegexService.match(phone.getPhoneNumber());
        if (!isValid) return new IUDAnswer(false, WRONG_PHONE_NUMBER);
        Integer phoneId = null;
        try {
            phoneId = phoneDAO.insertPhone(phone);
        } catch (IllegalArgumentException e) {
            return phoneDAO.rollback(WRONG_FIELD, e);
        }
        phoneDAO.commit();
        return new IUDAnswer(phoneId,true);
    }

    @Override
    public IUDAnswer updatePhone(Integer id, Phone newPhone) {
        if (newPhone == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        boolean isValid = phoneRegexService.match(newPhone.getPhoneNumber());
        if (!isValid) return new IUDAnswer(id, false, WRONG_PHONE_NUMBER);
        try {
            newPhone.setObjectId(id);
            Phone oldPhone = phoneDAO.getPhone(id);
            phoneDAO.updatePhone(newPhone, oldPhone);
        } catch (IllegalArgumentException e) {
            return phoneDAO.rollback(WRONG_FIELD, e);
        }
        phoneDAO.commit();
        return new IUDAnswer(id,true);
    }
}
