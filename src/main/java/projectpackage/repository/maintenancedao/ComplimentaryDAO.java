package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface ComplimentaryDAO {
    public Complimentary getComplimentary(Integer id);
    public List<Complimentary> getAllComplimentaries();
    public int insertComplimentary(Complimentary complimentary) throws TransactionException;
    public void updateComplimentary(Complimentary newComplimentary, Complimentary oldComplimentary) throws TransactionException;
    public void deleteComplimentary(int id) throws ReferenceBreakException;
}
