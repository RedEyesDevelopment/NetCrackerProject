package projectpackage.service.authservice;

import projectpackage.model.auth.Role;

import java.util.List;

public interface RoleService {
    public List<Role> getAllRoles();
    public List<Role> getAllRoles(String orderingParameter, boolean ascend);
    public Role getSingleRoleById(Integer id);
    public Role getSingleRoleByRoleName(String rolename);
}
