package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface ComplimentaryDAO {
    public Complimentary getComplimentary(Integer id);
    public List<Complimentary> getAllComplimentaries();
    public Integer insertComplimentary(Complimentary complimentary) throws TransactionException;
    public Integer updateComplimentary(Complimentary newComplimentary, Complimentary oldComplimentary) throws TransactionException;
    public void deleteComplimentary(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
