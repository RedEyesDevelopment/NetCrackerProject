package projectpackage.repository.authdao;

import projectpackage.model.auth.Role;

import java.util.List;

/**
 * Created by Arizel on 21.05.2017.
 */
public interface RoleDAO {
    public Role getRole(Integer id);
    public List<Role> getAllRoles();
}
