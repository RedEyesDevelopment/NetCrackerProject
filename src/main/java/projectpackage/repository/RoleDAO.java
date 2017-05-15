package projectpackage.repository;

import projectpackage.model.auth.Role;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface RoleDAO {
    public void insertRole(Role role);
    public void updateRole(Role newRole, Role oldRole);
}
