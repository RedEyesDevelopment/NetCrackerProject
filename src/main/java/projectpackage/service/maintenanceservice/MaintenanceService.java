package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.support.IUDAnswer;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface MaintenanceService {

    public List<Maintenance> getAllMaintenances();
    public List<Maintenance> getAllMaintenances(String orderingParameter, boolean ascend);
    public Maintenance getSingleMaintenanceById(int id);
    public IUDAnswer deleteMaintenance(int id);
    public IUDAnswer insertMaintenance(Maintenance maintenance);
    public IUDAnswer updateMaintenance(int id, Maintenance newMaintenance);
}
