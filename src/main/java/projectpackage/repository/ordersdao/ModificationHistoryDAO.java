package projectpackage.repository.ordersdao;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface ModificationHistoryDAO extends Commitable, Rollbackable{
    public ModificationHistory getModificationHistory(Integer id);
    public List<ModificationHistory> getAllModificationHistories();
    public Integer insertModificationHistory(Order newOrder, Order oldOrder);
    public void deleteModificationHistory(Integer id);
}
