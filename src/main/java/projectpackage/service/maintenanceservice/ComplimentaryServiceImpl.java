package projectpackage.service.maintenanceservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;

import java.util.List;

/**
 * Created by Dmitry on 21.05.2017.
 */
@Service
public class ComplimentaryServiceImpl implements ComplimentaryService {
    @Autowired
    ComplimentaryDAO complimentaryDAO;
    @Override
    public List<Complimentary> getAllComplimentaries() {
        return null;
    }

    @Override
    public Complimentary getSingleComplimentaryById(int id) {
        return null;
    }

    @Override
    public boolean deleteComplimentary(int id) {
        return false;
    }

    @Override
    public boolean insertComplimentary(Complimentary complimentary) {
        return false;
    }

    @Override
    public boolean updateComplimentary(int id, Complimentary newComplimentary) {
        return false;
    }
}
