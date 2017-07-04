package projectpackage.service.maintenanceservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;

import java.util.List;

@Service
@Log4j
public class ComplimentaryServiceImpl implements ComplimentaryService {
    private static final Logger LOGGER = Logger.getLogger(ComplimentaryServiceImpl.class);

    @Autowired
    ComplimentaryDAO complimentaryDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Complimentary> getAllComplimentaries() {
        List<Complimentary> complimentaries = complimentaryDAO.getAllComplimentaries();
        if (null == complimentaries) {
            LOGGER.info("Returned NULL!!!");
        }
        return complimentaries;
    }

    @Transactional(readOnly = true)
    @Override
    public Complimentary getSingleComplimentaryById(Integer id) {
        Complimentary complimentary = complimentaryDAO.getComplimentary(id);
        if (null == complimentary) {
            LOGGER.info("Returned NULL!!!");
        }
        return complimentary;
    }

    @Transactional
    @Override
    public IUDAnswer deleteComplimentary(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        complimentaryDAO.deleteComplimentary(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertComplimentary(Complimentary complimentary) {
        if (complimentary == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        Integer complimentaryId = complimentaryDAO.insertComplimentary(complimentary);

        return new IUDAnswer(complimentaryId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateComplimentary(Integer id, Complimentary newComplimentary) {
        if (newComplimentary == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        newComplimentary.setObjectId(id);
        Complimentary oldComplimentary = complimentaryDAO.getComplimentary(id);
        complimentaryDAO.updateComplimentary(newComplimentary, oldComplimentary);

        return new IUDAnswer(id,true);
    }
}
