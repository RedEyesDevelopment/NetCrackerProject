package projectpackage.repository.authdao;

import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.model.auth.Phone;

import java.util.List;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO {
    public Phone getPhone(Integer id);
    public List<Phone> getAllPhones();
    public int insertPhone(Phone phone) throws TransactionException;
    public void updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException;
    public void deletePhone(int id) throws ReferenceBreakException;
}
