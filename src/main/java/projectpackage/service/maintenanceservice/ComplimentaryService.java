package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Complimentary;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

/**
 * Created by Dmitry on 21.05.2017.
 */
public interface ComplimentaryService extends MessageBook{
    public List<Complimentary> getAllComplimentaries();
    public Complimentary getSingleComplimentaryById(Integer id);
    public IUDAnswer deleteComplimentary(Integer id);
    public IUDAnswer insertComplimentary(Complimentary complimentary);
    public IUDAnswer updateComplimentary(Integer id, Complimentary newComplimentary);
}
