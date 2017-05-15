package projectpackage.repository;

import projectpackage.repository.reacdao.exceptions.TransactionException;
import projectpackage.model.auth.Role;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface RoleDAO {
    public void insertRole(Role role) throws TransactionException;
    public void updateRole(Role newRole, Role oldRole) throws TransactionException;
}
