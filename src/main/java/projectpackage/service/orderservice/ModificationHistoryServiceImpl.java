package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.ordersdao.ModificationHistoryDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

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
    public List<ModificationHistory> getAllModificationHistory() {
        List<ModificationHistory> modificationHistories = modificationHistoryDAO.getAllModificationHistories();
        if (modificationHistories == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return modificationHistories;
    }

    @Override
    public ModificationHistory getSingleModificationHistoryById(Integer id) {
        ModificationHistory modificationHistory = modificationHistoryDAO.getModificationHistory(id);
        if (modificationHistory == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return modificationHistory;
    }

    @Override
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder) {
        if (newOrder == null || oldOrder == null) {
            return null;
        }
        Integer modificationId = null;
        try {
            modificationId = modificationHistoryDAO.insertModificationHistory(newOrder, oldOrder);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        modificationHistoryDAO.commit();
        return new IUDAnswer(modificationId,true);
    }

    @Override
    public IUDAnswer deleteModificationHistory(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            modificationHistoryDAO.deleteModificationHistory(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
        }
        modificationHistoryDAO.commit();
        return new IUDAnswer(id, true);
    }
}
