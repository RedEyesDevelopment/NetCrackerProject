package projectpackage.service;

import projectpackage.model.auth.Phone;

import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface PhoneService {
    public List<Phone> getAllPhones(String orderingParameter, boolean ascend);
    public Phone getSinglePhoneById(int id);
    public int deletePhoneById(int id);
    public boolean insertPhone(Phone phone);
    public boolean updatePhone(Phone newPhone);
}
