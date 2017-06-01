package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Complimentary;
import projectpackage.dto.IUDAnswer;

import java.util.List;

/**
 * Created by Dmitry on 21.05.2017.
 */
public interface ComplimentaryService {

    public List<Complimentary> getAllComplimentaries();//TODO Merlyan
    public Complimentary getSingleComplimentaryById(int id);
    public IUDAnswer deleteComplimentary(int id);
    public IUDAnswer insertComplimentary(Complimentary complimentary);
    public IUDAnswer updateComplimentary(int id, Complimentary newComplimentary);
}
