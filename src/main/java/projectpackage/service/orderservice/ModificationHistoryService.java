package projectpackage.service.orderservice;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface ModificationHistoryService extends MessageBook{
    public List<ModificationHistory> getAllModificationHistoryByOrder(Order order);

    public List<ModificationHistory> getAllModificationHistory();
    public List<ModificationHistory> getAllModificationHistory(String orderingParameter, boolean ascend);
    public ModificationHistory getSingleModificationHistoryById(Integer id);
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder);
    public IUDAnswer deleteModificationHistory(Integer id);
}
