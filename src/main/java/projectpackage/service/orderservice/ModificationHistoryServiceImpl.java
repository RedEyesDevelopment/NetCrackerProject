package projectpackage.service.orderservice;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class ModificationHistoryServiceImpl implements ModificationHistoryService{
    @Override
    public List<ModificationHistory> getAllModificationHistory(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<ModificationHistory> getAllModificationHistory() {
        return null;
    }

    @Override
    public List<ModificationHistory> getAllModificationHistoryByOrder(Order order) {
        return null;
    }

    @Override
    public ModificationHistory getSingleModificationHistoryById(int id) {
        return null;
    }
}