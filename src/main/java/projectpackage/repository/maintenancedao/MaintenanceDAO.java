package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface MaintenanceDAO {
    public Maintenance getMaintenance(Integer id);
    public List<Maintenance> getAllMaintenances();
    public int insertMaintenance(Maintenance maintenance) throws TransactionException;
    public void updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance) throws TransactionException;
    public int deleteMaintenance(int id);//TODO доделать проверки на связи
}
