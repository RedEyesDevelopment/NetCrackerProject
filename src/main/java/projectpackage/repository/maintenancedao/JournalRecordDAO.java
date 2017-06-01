package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.JournalRecord;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface JournalRecordDAO {
    public JournalRecord getJournalRecord(Integer id);
    public List<JournalRecord> getAllJournalRecords();
    public int insertJournalRecord(JournalRecord journalRecord) throws TransactionException;
    public void updateJournalRecord(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) throws TransactionException;
    public void deleteJournalRecord(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
