package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
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
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder) {
        Integer modificationId = null;
        try {
            modificationId = modificationHistoryDAO.insertModificationHistory(newOrder, oldOrder);
            LOGGER.info("Get from DB modificationHistoryId = " + modificationId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(modificationId,false, "transactionInterrupt");
        }
        return new IUDAnswer(modificationId,true);
    }

    @Override
    public IUDAnswer deleteModificationHistory(int id) {
        try {
            modificationHistoryDAO.deleteModificationHistory(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }
}
