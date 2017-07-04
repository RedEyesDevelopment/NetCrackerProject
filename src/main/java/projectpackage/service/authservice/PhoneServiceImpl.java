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
import projectpackage.service.regex.RegexService;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class PhoneServiceImpl implements PhoneService{
    private static final Logger LOGGER = Logger.getLogger(PhoneServiceImpl.class);

    @Autowired
    RegexService regexService;

    @Autowired
    PhoneDAO phoneDAO;

    @Override
    public List<Phone> getAllPhones() {
        List<Phone> phones = phoneDAO.getAllPhones();
        if (phones == null) {
            LOGGER.info("Returned NULL!!!");
        }
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
        if (phone == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return phone;
    }

    @Override
    public IUDAnswer deletePhone(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            phoneDAO.deletePhone(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
        }
        phoneDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertPhone(Phone phone) {
        if (phone == null) {
            return null;
        }
        boolean isValid = regexService.isValidPhone(phone.getPhoneNumber());
        if (!isValid) {
            return new IUDAnswer(false, WRONG_PHONE_NUMBER);
        }
        Integer phoneId = null;
        try {
            phoneId = phoneDAO.insertPhone(phone);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        phoneDAO.commit();
        return new IUDAnswer(phoneId,true);
    }

    @Override
    public IUDAnswer updatePhone(Integer id, Phone newPhone) {
        if (newPhone == null) {
            return null;
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        boolean isValid = regexService.isValidPhone(newPhone.getPhoneNumber());
        if (!isValid) {
            return new IUDAnswer(id, false, WRONG_PHONE_NUMBER);
        }
        try {
            newPhone.setObjectId(id);
            Phone oldPhone = phoneDAO.getPhone(id);
            phoneDAO.updatePhone(newPhone, oldPhone);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id,false, WRONG_FIELD, e.getMessage());
        }
        phoneDAO.commit();
        return new IUDAnswer(id,true);
    }
}
