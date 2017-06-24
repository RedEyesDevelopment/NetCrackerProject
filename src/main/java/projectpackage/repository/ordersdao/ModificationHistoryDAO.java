package projectpackage.repository.ordersdao;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface ModificationHistoryDAO {
    public ModificationHistory getModificationHistory(Integer id);
    public List<ModificationHistory> getAllModificationHistories();
    public Integer insertModificationHistory(Order newOrder, Order oldOrder) throws TransactionException;
    public void deleteModificationHistory(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
