package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.JournalRecord;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface JournalRecordService {

    public List<JournalRecord> getAllJournalRecords();
    public List<JournalRecord> getAllJournalRecords(String orderingParameter, boolean ascend);
    public JournalRecord getSingleEntityById(int id);
    public boolean deleteJournalRecord(int id);
    public boolean insertJournalRecord(JournalRecord journalRecord);
    public boolean updateJournalRecord(int id, JournalRecord newJournalRecord);
}
