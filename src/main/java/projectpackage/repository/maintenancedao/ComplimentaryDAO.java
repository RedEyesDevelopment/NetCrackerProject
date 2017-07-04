package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface ComplimentaryDAO extends Commitable, Rollbackable{
    public Complimentary getComplimentary(Integer id);
    public List<Complimentary> getAllComplimentaries();
    public Integer insertComplimentary(Complimentary complimentary);
    public Integer updateComplimentary(Complimentary newComplimentary, Complimentary oldComplimentary);
    public void deleteComplimentary(Integer id);
}
