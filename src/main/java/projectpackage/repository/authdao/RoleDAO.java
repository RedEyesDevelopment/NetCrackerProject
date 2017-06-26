package projectpackage.repository.authdao;

import projectpackage.model.auth.Role;

import java.util.List;
import java.util.Optional;

/**
 * Created by Arizel on 21.05.2017.
 */
public interface RoleDAO {
    public Optional<Role> getRole(Integer id);
    public List<Optional<Role>> getAllRoles();
}
