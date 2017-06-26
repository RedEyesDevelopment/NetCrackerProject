package projectpackage.repository.authdao;

import projectpackage.model.auth.Phone;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;
import java.util.Optional;

/**
 * Created by Lenovo on 04.05.2017.
 */
public interface PhoneDAO {
    public Optional<Phone> getPhone(Integer id);
    public List<Optional<ReactEntityWithId>> getAllPhones();
    public int insertPhone(Phone phone) throws TransactionException;
    public void updatePhone(Phone newPhone, Phone oldPhone) throws TransactionException;
    public void deletePhone(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
