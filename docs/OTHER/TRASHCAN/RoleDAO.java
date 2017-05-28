package projectpackage.repository;

import projectpackage.model.auth.Role;
import projectpackage.repository.reacdao.exceptions.TransactionException;

/**
 * Created by Lenovo on 15.05.2017.
 */
public interface RoleDAO {
    public void insertRole(Role role) throws TransactionException;
    public void updateRole(Role newRole, Role oldRole) throws TransactionException;
}
