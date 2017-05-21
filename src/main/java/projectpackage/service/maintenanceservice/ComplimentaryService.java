package projectpackage.service.maintenanceservice;

import projectpackage.model.maintenances.Complimentary;

import java.util.List;

/**
 * Created by Dmitry on 21.05.2017.
 */
public interface ComplimentaryService {

    public List<Complimentary> getAllComplimentaries();//TODO Merlyan
    public Complimentary getSingleComplimentaryById(int id);
    public boolean deleteComplimentary(int id);
    public boolean insertComplimentary(Complimentary complimentary);
    public boolean updateComplimentary(int id, Complimentary newComplimentary);
}
