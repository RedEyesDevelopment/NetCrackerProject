package projectpackage.service;

import projectpackage.model.auth.Role;

import java.util.List;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface RoleService {
    public List<Role> getAllRoles(String orderingParameter, boolean ascend);
    public Role getSingleRoleById(int id);
    public Role getSingleRoleByRolename(String rolename);
    public int deleteRoleById(int id);
    public boolean insertRole(Role role);
    public boolean updateRole(Role newRole);
}
