package projectpackage.repository.authdao;

import projectpackage.model.auth.User;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;

import java.util.List;

public interface UserDAO {
    public User getUser(Integer id);
    public List<User> getAllUsers();
    public int insertUser(User user) throws TransactionException;
    public void updateUser(User newUser, User oldUser) throws TransactionException;
    public void deleteUser(int id) throws ReferenceBreakException;
}
