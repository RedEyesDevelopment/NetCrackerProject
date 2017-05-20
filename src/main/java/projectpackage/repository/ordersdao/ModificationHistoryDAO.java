package projectpackage.repository.ordersdao;

import projectpackage.model.orders.ModificationHistory;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface ModificationHistoryDAO {
    public ModificationHistory getModificationHistory(Integer id);
    public List<ModificationHistory> getAllModificationHistories();
    public int insertModificationHistory(ModificationHistory modificationHistory) throws TransactionException;
    public void updateModificationHistory(ModificationHistory newModificationHistory, ModificationHistory oldModificationHistory) throws TransactionException;
    public int deleteModificationHistory(int id);
}
