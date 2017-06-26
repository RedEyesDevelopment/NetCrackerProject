package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.JournalRecord;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface JournalRecordDAO extends Commitable, Rollbackable{
    public JournalRecord getJournalRecord(Integer id);
    public List<JournalRecord> getAllJournalRecords();
    public Integer insertJournalRecord(JournalRecord journalRecord);
    public Integer updateJournalRecord(JournalRecord newJournalRecord, JournalRecord oldJournalRecord);
    public void deleteJournalRecord(Integer id);
}
