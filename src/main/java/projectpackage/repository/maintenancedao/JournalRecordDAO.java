package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.JournalRecord;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface JournalRecordDAO {
    public JournalRecord getJournalRecord(Integer id);
    public List<JournalRecord> getAllJournalRecords();
    public int insertJournalRecord(JournalRecord journalRecord) throws TransactionException;
    public void updateJournalRecord(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) throws TransactionException;
    public int deleteJournalRecord(int id);
}
