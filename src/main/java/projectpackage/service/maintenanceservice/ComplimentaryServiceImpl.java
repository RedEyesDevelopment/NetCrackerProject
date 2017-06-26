package projectpackage.service.maintenanceservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

/**
 * Created by Dmitry on 21.05.2017.
 */
@Service
@Log4j
public class ComplimentaryServiceImpl implements ComplimentaryService {
    private static final Logger LOGGER = Logger.getLogger(ComplimentaryServiceImpl.class);

    @Autowired
    ComplimentaryDAO complimentaryDAO;

    @Override
    public List<Complimentary> getAllComplimentaries() {
        List<Complimentary> complimentaries = complimentaryDAO.getAllComplimentaries();
        if (null == complimentaries) LOGGER.info("Returned NULL!!!");
        return complimentaries;
    }

    @Override
    public Complimentary getSingleComplimentaryById(Integer id) {
        Complimentary complimentary = complimentaryDAO.getComplimentary(id);
        if (null == complimentary) LOGGER.info("Returned NULL!!!");
        return complimentary;
    }

    @Override
    public IUDAnswer deleteComplimentary(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            complimentaryDAO.deleteComplimentary(id);
        } catch (ReferenceBreakException e) {
            return complimentaryDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return complimentaryDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return complimentaryDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return complimentaryDAO.rollback(id, NULL_ID, e);
        }
        complimentaryDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertComplimentary(Complimentary complimentary) {
        if (complimentary == null) return null;
        Integer complimentaryId = null;
        try {
            complimentaryId = complimentaryDAO.insertComplimentary(complimentary);
            LOGGER.info("Get from DB complimentaryId = " + complimentaryId);
        } catch (IllegalArgumentException e) {
            return complimentaryDAO.rollback(WRONG_FIELD, e);
        }
        complimentaryDAO.commit();
        return new IUDAnswer(complimentaryId,true);
    }

    @Override
    public IUDAnswer updateComplimentary(Integer id, Complimentary newComplimentary) {
        if (newComplimentary == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newComplimentary.setObjectId(id);
            Complimentary oldComplimentary = complimentaryDAO.getComplimentary(id);
            complimentaryDAO.updateComplimentary(newComplimentary, oldComplimentary);
        } catch (IllegalArgumentException e) {
            return complimentaryDAO.rollback(WRONG_FIELD, e);
        }
        complimentaryDAO.commit();
        return new IUDAnswer(id,true);
    }
}
