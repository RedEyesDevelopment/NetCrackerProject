package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.repository.AbstractDAO;

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
            return (Role) manager.createReactEAV(Role.class).getSingleEntityWithId(id);
    }

    @Override
    public List<Role> getAllRoles() {
            return manager.createReactEAV(Role.class).getEntityCollection();
    }
}
