package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.ordersdao.ModificationHistoryDAO;

import java.util.List;

@Log4j
@Service
public class ModificationHistoryServiceImpl implements ModificationHistoryService{
    private static final Logger LOGGER = Logger.getLogger(ModificationHistoryServiceImpl.class);

    @Autowired
    ModificationHistoryDAO modificationHistoryDAO;

    @Transactional(readOnly = true)
    @Override
    public List<ModificationHistory> getAllModificationHistory() {
        List<ModificationHistory> modificationHistories = modificationHistoryDAO.getAllModificationHistories();
        if (modificationHistories == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return modificationHistories;
    }

    @Transactional(readOnly = true)
    @Override
    public ModificationHistory getSingleModificationHistoryById(Integer id) {
        ModificationHistory modificationHistory = modificationHistoryDAO.getModificationHistory(id);
        if (modificationHistory == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return modificationHistory;
    }

    @Transactional
    @Override
    public IUDAnswer insertModificationHistory(Order newOrder, Order oldOrder) {
        if (newOrder == null || oldOrder == null) {
            return null;
        }
        Integer modificationId = null;

        modificationId = modificationHistoryDAO.insertModificationHistory(newOrder, oldOrder);

        return new IUDAnswer(modificationId,true);
    }

    @Transactional
    @Override
    public IUDAnswer deleteModificationHistory(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        modificationHistoryDAO.deleteModificationHistory(id);

        return new IUDAnswer(id, true);
    }
}
