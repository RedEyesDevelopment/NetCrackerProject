package projectpackage.service.maintenanceservice;

import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.service.MessageBook;

import java.util.List;

public interface JournalRecordService extends MessageBook{
    public List<JournalRecord> getAllJournalRecords();
    public List<JournalRecord> getJournalRecordsByOrder(Integer orderId);
    public JournalRecord getSingleEntityById(Integer id);
    public IUDAnswer deleteJournalRecord(Integer id);
    public IUDAnswer insertJournalRecord(JournalRecord journalRecord);
    public IUDAnswer updateJournalRecord(Integer id, JournalRecord newJournalRecord);
}
