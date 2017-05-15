package projectpackage.repository;

import projectpackage.model.auth.Phone;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO {
    public void insertPhone(Phone phone);
    public void updatePhone(Phone newPhone, Phone oldPhone);
}
