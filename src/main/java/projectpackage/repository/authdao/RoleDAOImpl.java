package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;


/**
 * Created by Arizel on 21.05.2017.
 */
@Repository
public class RoleDAOImpl extends AbstractDAO implements RoleDAO{
    private static final Logger LOGGER = Logger.getLogger(RoleDAOImpl.class);

    @Override
    public Role getRole(Integer id) {
        if (id == null) return null;
        try {
            return (Role) manager.createReactEAV(Role.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Role> getAllRoles() {
        try {
            return manager.createReactEAV(Role.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }
}
