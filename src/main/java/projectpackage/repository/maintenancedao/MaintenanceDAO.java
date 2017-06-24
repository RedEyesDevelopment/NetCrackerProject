package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Maintenance;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface MaintenanceDAO {
    public Maintenance getMaintenance(Integer id);
    public List<Maintenance> getAllMaintenances();
    public Integer insertMaintenance(Maintenance maintenance);
    public Integer updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance);
    public void deleteMaintenance(Integer id);
}
