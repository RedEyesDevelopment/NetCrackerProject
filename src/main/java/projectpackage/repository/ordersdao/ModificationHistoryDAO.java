package projectpackage.repository.ordersdao;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface ModificationHistoryDAO {
    public ModificationHistory getModificationHistory(Integer id);
    public List<ModificationHistory> getAllModificationHistories();
    public Integer insertModificationHistory(Order newOrder, Order oldOrder);
    public void deleteModificationHistory(Integer id);
}
