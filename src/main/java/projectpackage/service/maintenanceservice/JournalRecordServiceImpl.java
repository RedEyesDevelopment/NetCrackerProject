package projectpackage.service.maintenanceservice;


import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Order;
import projectpackage.repository.maintenancedao.JournalRecordDAO;
import projectpackage.repository.ordersdao.OrderDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dima on 21.05.2017.
 */
@Log4j
@Service
public class JournalRecordServiceImpl implements JournalRecordService{
    private static final Logger LOGGER = Logger.getLogger(JournalRecordServiceImpl.class);

    @Autowired
    JournalRecordDAO journalRecordDAO;

    @Autowired
    OrderDAO orderDAO;

    @Override
    public List<JournalRecord> getAllJournalRecords() {
        List<JournalRecord> journalRecords = journalRecordDAO.getAllJournalRecords();
        if (null == journalRecords) LOGGER.info("Returned NULL!!!");
        return journalRecords;
    }

    @Override
    public List<JournalRecord> getJournalRecordsByOrder(Integer orderId) {
        List<JournalRecord> answer = new ArrayList<>();
        List<JournalRecord> allJournalRecords = getAllJournalRecords();
        for (JournalRecord journalRecord : allJournalRecords) {
            if (journalRecord.getOrderId() == orderId) {
                answer.add(journalRecord);
            }
        }
        return answer;
    }

    @Override
    public JournalRecord getSingleEntityById(Integer id) {
        JournalRecord journalRecord = journalRecordDAO.getJournalRecord(id);
        if (null == journalRecord) LOGGER.info("Returned NULL!!!");
        return journalRecord;
    }

    @Override
    public IUDAnswer deleteJournalRecord(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            journalRecordDAO.deleteJournalRecord(id);
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
        journalRecordDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertJournalRecord(JournalRecord journalRecord) {
        if (journalRecord == null) return null;
        Order order = orderDAO.getOrder(journalRecord.getOrderId());
        Set<Complimentary> freeComplimentaries = order.getCategory().getComplimentaries();
        Set<Maintenance> freeMaintenances = new HashSet<>();
        for (Complimentary freeComplimentary : freeComplimentaries) {
            freeMaintenances.add(freeComplimentary.getMaintenance());
        }
        if (freeMaintenances.contains(journalRecord.getMaintenance())) {
            journalRecord.setCost(0L);
        } else {
            journalRecord.setCost(journalRecord.getCount() * journalRecord.getMaintenance().getMaintenancePrice());
        }
        Integer journalRecordId = null;
        try {
            journalRecordId = journalRecordDAO.insertJournalRecord(journalRecord);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        journalRecordDAO.commit();
        return new IUDAnswer(journalRecordId,true);
    }

    @Override
    public IUDAnswer updateJournalRecord(Integer id, JournalRecord newJournalRecord) {
        if (newJournalRecord == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newJournalRecord.setObjectId(id);
            JournalRecord oldJournalRecord = journalRecordDAO.getJournalRecord(id);
            journalRecordDAO.updateJournalRecord(newJournalRecord, oldJournalRecord);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
        }
        journalRecordDAO.commit();
        return new IUDAnswer(id,true);
    }
}
