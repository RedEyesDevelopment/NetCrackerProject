package projectpackage.repository.authdao;

import projectpackage.model.auth.Role;

import java.util.List;

public interface RoleDAO {
    public Role getRole(Integer id);
    public List<Role> getAllRoles();
}
