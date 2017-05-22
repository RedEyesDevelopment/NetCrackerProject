package projectpackage.service.orderservice;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface ModificationHistoryService {
    public List<ModificationHistory> getAllModificationHistoryByOrder(Order order);

    public List<ModificationHistory> getAllModificationHistory();
    public List<ModificationHistory> getAllModificationHistory(String orderingParameter, boolean ascend);
    public ModificationHistory getSingleModificationHistoryById(int id);
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder) throws TransactionException;
    public IUDAnswer deleteModificationHistory(int id) throws ReferenceBreakException;
}
