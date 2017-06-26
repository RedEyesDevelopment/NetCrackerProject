package projectpackage.repository.authdao;

import projectpackage.model.auth.Phone;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO extends Commitable, Rollbackable{
    public Phone getPhone(Integer id);
    public List<Phone> getAllPhones();
    public Integer insertPhone(Phone phone);
    public Integer updatePhone(Phone newPhone, Phone oldPhone);
    public void deletePhone(Integer id);
}
