package projectpackage.service.orderservice;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface ModificationHistoryService {
    public List<ModificationHistory> getAllModificationHistoryByOrder(Order order);//TODO Denis

    public List<ModificationHistory> getAllModificationHistory();//TODO Pacanu
    public List<ModificationHistory> getAllModificationHistory(String orderingParameter, boolean ascend);//TODO Pacanu
    public ModificationHistory getSingleModificationHistoryById(int id);//TODO Pacanu
}
