package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface MaintenanceDAO extends Commitable, Rollbackable{
    public Maintenance getMaintenance(Integer id);
    public List<Maintenance> getAllMaintenances();
    public Integer insertMaintenance(Maintenance maintenance);
    public Integer updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance);
    public void deleteMaintenance(Integer id);
}
