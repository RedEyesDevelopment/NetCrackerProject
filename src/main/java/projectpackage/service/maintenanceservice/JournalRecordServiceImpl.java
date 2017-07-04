package projectpackage.service.maintenanceservice;


import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Order;
import projectpackage.repository.maintenancedao.JournalRecordDAO;
import projectpackage.repository.ordersdao.OrderDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j
@Service
public class JournalRecordServiceImpl implements JournalRecordService{
    private static final Logger LOGGER = Logger.getLogger(JournalRecordServiceImpl.class);

    @Autowired
    JournalRecordDAO journalRecordDAO;

    @Autowired
    OrderDAO orderDAO;

    @Transactional(readOnly = true)
    @Override
    public List<JournalRecord> getAllJournalRecords() {
        List<JournalRecord> journalRecords = journalRecordDAO.getAllJournalRecords();
        if (null == journalRecords) {
            LOGGER.info("Returned NULL!!!");
        }
        return journalRecords;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public JournalRecord getSingleEntityById(Integer id) {
        JournalRecord journalRecord = journalRecordDAO.getJournalRecord(id);
        if (null == journalRecord) {
            LOGGER.info("Returned NULL!!!");
        }
        return journalRecord;
    }

    @Transactional
    @Override
    public IUDAnswer deleteJournalRecord(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        journalRecordDAO.deleteJournalRecord(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertJournalRecord(JournalRecord journalRecord) {
        if (journalRecord == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
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
        Integer journalRecordId = journalRecordDAO.insertJournalRecord(journalRecord);

        return new IUDAnswer(journalRecordId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateJournalRecord(Integer id, JournalRecord newJournalRecord) {
        if (newJournalRecord == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        newJournalRecord.setObjectId(id);
        JournalRecord oldJournalRecord = journalRecordDAO.getJournalRecord(id);
        journalRecordDAO.updateJournalRecord(newJournalRecord, oldJournalRecord);

        return new IUDAnswer(id,true);
    }
}
