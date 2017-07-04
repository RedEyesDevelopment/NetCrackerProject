package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
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

    @Transactional(readOnly = true)
    @Override
    public List<Phone> getAllPhones() {
        List<Phone> phones = phoneDAO.getAllPhones();
        if (phones == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return phones;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Phone getSinglePhoneById(Integer id) {
        Phone phone = phoneDAO.getPhone(id);
        if (phone == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return phone;
    }

    @Transactional
    @Override
    public IUDAnswer deletePhone(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        phoneDAO.deletePhone(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertPhone(Phone phone) {
        if (phone == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        boolean isValid = regexService.isValidPhone(phone.getPhoneNumber());
        if (!isValid) {
            return new IUDAnswer(false, WRONG_PHONE_NUMBER);
        }
        Integer phoneId = phoneDAO.insertPhone(phone);

        return new IUDAnswer(phoneId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updatePhone(Integer id, Phone newPhone) {
        if (newPhone == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        boolean isValid = regexService.isValidPhone(newPhone.getPhoneNumber());
        if (!isValid) {
            return new IUDAnswer(id, false, WRONG_PHONE_NUMBER);
        }

        newPhone.setObjectId(id);
        Phone oldPhone = phoneDAO.getPhone(id);
        phoneDAO.updatePhone(newPhone, oldPhone);

        return new IUDAnswer(id,true);
    }
}
