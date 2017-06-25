package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Maintenance;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
public interface MaintenanceService extends MessageBook{

    public List<Maintenance> getAllMaintenances();
    public List<Maintenance> getAllMaintenances(String orderingParameter, boolean ascend);
    public Maintenance getSingleMaintenanceById(Integer id);
    public IUDAnswer deleteMaintenance(Integer id);
    public IUDAnswer insertMaintenance(Maintenance maintenance);
    public IUDAnswer updateMaintenance(Integer id, Maintenance newMaintenance);
}
