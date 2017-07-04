package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Maintenance;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface MaintenanceService extends MessageBook{
    public List<Maintenance> getAllMaintenances();
    public Maintenance getSingleMaintenanceById(Integer id);
    public IUDAnswer deleteMaintenance(Integer id);
    public IUDAnswer insertMaintenance(Maintenance maintenance);
    public IUDAnswer updateMaintenance(Integer id, Maintenance newMaintenance);
}
