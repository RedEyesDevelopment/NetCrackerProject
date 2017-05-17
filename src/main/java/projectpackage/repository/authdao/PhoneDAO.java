package projectpackage.repository.authdao;

import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.model.auth.Phone;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO {
    public int insertPhone(Phone phone) throws TransactionException;
    public void updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException;
    public int deletePhone(int id);
}
