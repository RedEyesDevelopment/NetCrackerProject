package projectpackage.service.authservice;

import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface PhoneService extends MessageBook{
    public List<Phone> getAllPhonesByUser(User user);
    public List<Phone> getAllPhones();
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend);
    public Phone getSinglePhoneById(Integer id);
    public IUDAnswer deletePhone(Integer id);
    public IUDAnswer insertPhone(Phone phone);
    public IUDAnswer updatePhone(Integer id, Phone newPhone);
}
