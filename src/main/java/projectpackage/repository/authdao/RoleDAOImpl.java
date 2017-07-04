package projectpackage.repository.authdao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Role;
import projectpackage.repository.AbstractDAO;

import java.util.List;


@Repository
public class RoleDAOImpl extends AbstractDAO implements RoleDAO{
    private static final Logger LOGGER = Logger.getLogger(RoleDAOImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Role getRole(Integer id) {
        if (id == null) {
            return null;
        }
            return (Role) manager.createReactEAV(Role.class).getSingleEntityWithId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
            return manager.createReactEAV(Role.class).getEntityCollection();
    }
}
