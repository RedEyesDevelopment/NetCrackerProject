package projectpackage.repository.authdao;

import projectpackage.repository.reacdao.exceptions.TransactionException;
import projectpackage.model.auth.Phone;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO {
    public void insertPhone(Phone phone) throws TransactionException;
    public void updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException;
}
