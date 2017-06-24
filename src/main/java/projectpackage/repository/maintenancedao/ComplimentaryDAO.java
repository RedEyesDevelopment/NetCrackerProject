package projectpackage.repository.maintenancedao;

import projectpackage.model.maintenances.Complimentary;

import java.util.List;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface ComplimentaryDAO {
    public Complimentary getComplimentary(Integer id);
    public List<Complimentary> getAllComplimentaries();
    public Integer insertComplimentary(Complimentary complimentary);
    public Integer updateComplimentary(Complimentary newComplimentary, Complimentary oldComplimentary);
    public void deleteComplimentary(Integer id);
}
