package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.authdao.PhoneDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.support.PhoneRegexService;

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
        String email = user.getEmail();
        List<Phone> allUser = getAllPhones();
        for (Phone phone : allUser) {
            if (user.getPhones().contains(phone)) {
                answer.add(phone);
            }
        }
        return answer;
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
    public IUDAnswer deletePhone(int id) {
        try {
            phoneDAO.deletePhone(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(false, e.printReferencesEntities());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer insertPhone(Phone phone) {
        boolean isValid = phoneRegexService.match(phone.getPhoneNumber());
        if (!isValid) return new IUDAnswer(false, "Incorrect phone number!");

        try {
            int phoneId = phoneDAO.insertPhone(phone);
            LOGGER.info("Get from DB phoneId = " + phoneId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer updatePhone(int id, Phone newPhone) {
        boolean isValid = phoneRegexService.match(newPhone.getPhoneNumber());
        if (!isValid) return new IUDAnswer(false, "Incorrect phone number!");

        try {
            newPhone.setObjectId(id);
            Phone oldPhone = phoneDAO.getPhone(id);
            phoneDAO.updatePhone(newPhone, oldPhone);
            return new IUDAnswer(true);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
    }
}
