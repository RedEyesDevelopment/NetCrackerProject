package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Maintenance;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface MaintenanceService {

    public List<Maintenance> getAllMaintenances();
    public List<Maintenance> getAllMaintenances(String orderingParameter, boolean ascend);
    public Maintenance getSingleMaintenanceById(int id);
    public boolean deleteMaintenance(int id);
    public boolean insertMaintenance(Maintenance maintenance);
    public boolean updateMaintenance(int id, Maintenance newMaintenance);
}
