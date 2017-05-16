package projectpackage.service.authservice;

import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;

import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface PhoneService {
    public List<Phone> getAllPhonesByUser(User user);//TODO Denis

    public List<Phone> getAllPhones();//TODO Merlyan
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend);
    public Phone getSinglePhoneById(int id);
    public boolean deletePhone(int id);
    public boolean insertPhone(Phone phone);
    public boolean updatePhone(Phone newPhone);
}
