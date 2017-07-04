package projectpackage.service.orderservice;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface ModificationHistoryService extends MessageBook{
    public List<ModificationHistory> getAllModificationHistory();
    public ModificationHistory getSingleModificationHistoryById(Integer id);
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder);
    public IUDAnswer deleteModificationHistory(Integer id);
}
