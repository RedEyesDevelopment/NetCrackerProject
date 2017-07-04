package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Complimentary;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface ComplimentaryService extends MessageBook{
    public List<Complimentary> getAllComplimentaries();
    public Complimentary getSingleComplimentaryById(Integer id);
    public IUDAnswer deleteComplimentary(Integer id);
    public IUDAnswer insertComplimentary(Complimentary complimentary);
    public IUDAnswer updateComplimentary(Integer id, Complimentary newComplimentary);
}
