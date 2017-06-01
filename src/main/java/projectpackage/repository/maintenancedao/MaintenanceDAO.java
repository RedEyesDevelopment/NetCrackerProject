package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface MaintenanceDAO {
    public Maintenance getMaintenance(Integer id);
    public List<Maintenance> getAllMaintenances();
    public int insertMaintenance(Maintenance maintenance) throws TransactionException;
    public void updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance) throws TransactionException;
    public void deleteMaintenance(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;//TODO доделать проверки на связи
}
