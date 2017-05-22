package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ordersdao.ModificationHistoryDAO;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class ModificationHistoryServiceImpl implements ModificationHistoryService{
    private static final Logger LOGGER = Logger.getLogger(ModificationHistoryServiceImpl.class);

    @Autowired
    ModificationHistoryDAO modificationHistoryDAO;

    @Override
    public List<ModificationHistory> getAllModificationHistory(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<ModificationHistory> getAllModificationHistory() {
        List<ModificationHistory> modificationHistories = modificationHistoryDAO.getAllModificationHistories();
        if (modificationHistories == null) LOGGER.info("Returned NULL!!!");
        return modificationHistories;
    }

    @Override
    public List<ModificationHistory> getAllModificationHistoryByOrder(Order order) {
        return null;
    }

    @Override
    public ModificationHistory getSingleModificationHistoryById(int id) {
        ModificationHistory modificationHistory = modificationHistoryDAO.getModificationHistory(id);
        if (modificationHistory == null) LOGGER.info("Returned NULL!!!");
        return modificationHistory;
    }

    @Override
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder) throws TransactionException {
        try {
            modificationHistoryDAO.insertModificationHistory(newOrder, oldOrder);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer deleteModificationHistory(int id) throws ReferenceBreakException {
        try {
            modificationHistoryDAO.deleteModificationHistory(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(false, e.printReferencesEntities());
        }
        return new IUDAnswer(true);
    }
}
