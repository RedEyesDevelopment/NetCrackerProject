package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.support.IUDAnswer;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface JournalRecordService {

    public List<JournalRecord> getAllJournalRecords();
    public List<JournalRecord> getAllJournalRecords(String orderingParameter, boolean ascend);
    public JournalRecord getSingleEntityById(int id);
    public IUDAnswer deleteJournalRecord(int id);
    public IUDAnswer insertJournalRecord(JournalRecord journalRecord);
    public IUDAnswer updateJournalRecord(int id, JournalRecord newJournalRecord);
}
