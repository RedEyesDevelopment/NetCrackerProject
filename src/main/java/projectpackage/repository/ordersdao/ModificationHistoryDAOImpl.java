package projectpackage.repository.ordersdao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import projectpackage.model.orders.ModificationHistory;

import java.util.List;

/**
 * Created by Arizel on 21.05.2017.
 */
@Repository
public class ModificationHistoryDAOImpl implements ModificationHistoryDAO {
    private static final Logger LOGGER = Logger.getLogger(ModificationHistoryDAOImpl.class);

    @Override
    public ModificationHistory getModificationHistory(Integer id) {
        return null;
    }

    @Override
    public List<ModificationHistory> getAllModificationHistories() {
        return null;
    }

    @Override
    public void deleteModificationHistory(int id) {

    }
}
