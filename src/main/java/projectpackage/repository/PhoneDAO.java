package projectpackage.repository;

import projectpackage.model.auth.Phone;

import java.util.List;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO {
    public List<Phone> getPhonesList();
}
