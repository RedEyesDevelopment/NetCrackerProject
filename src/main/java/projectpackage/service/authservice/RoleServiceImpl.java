package projectpackage.service.authservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Role;
import projectpackage.repository.authdao.RoleDAO;

import java.util.List;

@Log4j
@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = Logger.getLogger(RoleServiceImpl.class);

    @Autowired
    RoleDAO roleDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = roleDAO.getAllRoles();
        if (roles == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return roles;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> getAllRoles(String orderingParameter, boolean ascend) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Role getSingleRoleById(Integer id) {
        Role role = roleDAO.getRole(id);
        if (role == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return role;
    }

    @Transactional(readOnly = true)
    @Override
    public Role getSingleRoleByRoleName(String rolename) {
        if (null == rolename) {
            return null;
        }
        List<Role> roles = getAllRoles("objectId", true);
        for (Role role:roles){
            if (rolename.equals(role.getRoleName())){
                return role;
            }
        }
        return null;
    }

}
