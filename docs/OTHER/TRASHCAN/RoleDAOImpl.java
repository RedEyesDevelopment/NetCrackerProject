package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.repository.reacdao.exceptions.TransactionException;

/**
 * Created by Lenovo on 15.05.2017.
 */
@Repository
public class RoleDAOImpl implements RoleDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void insertRole(Role role) throws TransactionException {
        try {
        } catch (NullPointerException e) {
            throw new TransactionException(role);
        }
    }

    @Override
    public void updateRole(Role newRole, Role oldRole) throws TransactionException {
        try {
        } catch (NullPointerException e) {
            throw new TransactionException(newRole);
        }
    }
}
