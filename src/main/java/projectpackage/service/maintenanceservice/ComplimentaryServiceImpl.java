package projectpackage.service.maintenanceservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.maintenancedao.ComplimentaryDAO;

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
    public Complimentary getSingleComplimentaryById(int id) {
        Complimentary complimentary = complimentaryDAO.getComplimentary(id);
        if (null == complimentary) LOGGER.info("Returned NULL!!!");
        return complimentary;
    }

    @Override
    public IUDAnswer deleteComplimentary(int id) {
        try {
            complimentaryDAO.deleteComplimentary(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertComplimentary(Complimentary complimentary) {
        Integer complimentaryId = null;
        try {
            complimentaryId = complimentaryDAO.insertComplimentary(complimentary);
            LOGGER.info("Get from DB complimentaryId = " + complimentaryId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(complimentaryId,false, "transactionInterrupt");
        }
        return new IUDAnswer(complimentaryId,true);
    }

    @Override
    public IUDAnswer updateComplimentary(int id, Complimentary newComplimentary) {
        try {
            newComplimentary.setObjectId(id);
            Complimentary oldComplimentary = complimentaryDAO.getComplimentary(id);
            complimentaryDAO.updateComplimentary(newComplimentary, oldComplimentary);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
