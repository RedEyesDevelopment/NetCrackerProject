package projectpackage.repository.authdao;

import projectpackage.model.auth.User;
import projectpackage.repository.support.daoexceptions.*;

import java.util.List;

public interface UserDAO {
    public User getUser(Integer id);
    public List<User> getAllUsers();
    public Integer insertUser(User user) throws TransactionException, DuplicateEmailException;
    public Integer updateUser(User newUser, User oldUser) throws TransactionException;
    public void deleteUser(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
