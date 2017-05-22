package projectpackage.service.orderservice;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface ModificationHistoryService {
    public List<ModificationHistory> getAllModificationHistoryByOrder(Order order);

    public List<ModificationHistory> getAllModificationHistory() throws ResultEntityNullException;
    public List<ModificationHistory> getAllModificationHistory(String orderingParameter, boolean ascend);
    public ModificationHistory getSingleModificationHistoryById(int id);
}
