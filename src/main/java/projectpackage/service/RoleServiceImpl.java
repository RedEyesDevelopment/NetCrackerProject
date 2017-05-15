package projectpackage.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.repository.reacdao.exceptions.TransactionException;
import projectpackage.model.auth.Role;
import projectpackage.repository.DeleteDAO;
import projectpackage.repository.RoleDAO;
import projectpackage.repository.reacdao.ReactEAVManager;
import projectpackage.repository.reacdao.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
@Log4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    DeleteDAO deleteDAO;

    @Autowired
    ReactEAVManager manager;

    @Override
    public List<Role> getAllRoles(String orderingParameter, boolean ascend) {
        List<Role> list = null;
        try {
            list = (List<Role>) manager.createReactEAV(Role.class).getEntityCollectionOrderByParameter(orderingParameter, ascend);
        } catch (ResultEntityNullException e) {
            log.warn("getAllRoles method returned null list", e);
        }
        return list;
    }

    @Override
    public Role getSingleRoleById(int id) {
        Role role=null;
        try {
            role = (Role) manager.createReactEAV(Role.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            log.warn("getSingleRoleById method returned null list", e);
        }
        return role;
    }

    @Override
    public Role getSingleRoleByRolename(String rolename) {
        if (null==rolename) return null;
        List<Role> roles = getAllRoles("objectId", true);
        for (Role role:roles){
            if (rolename.equals(role.getRoleName())){
                return role;
            }
        }
        return null;
    }

    @Override
    public int deleteRoleById(int id) {
        return deleteDAO.deleteSingleEntityById(id);
    }

    @Override
    public boolean insertRole(Role role) {
        try {
            roleDAO.insertRole(role);
        } catch (TransactionException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateRole(Role newRole) {
        try {
            Role oldRole = (Role) manager.createReactEAV(Role.class).getSingleEntityWithId(newRole.getObjectId());
            roleDAO.updateRole(newRole,oldRole);
        } catch (ResultEntityNullException e) {
            return false;
        } catch (TransactionException e) {
            return false;
        }
        return true;
    }
}
