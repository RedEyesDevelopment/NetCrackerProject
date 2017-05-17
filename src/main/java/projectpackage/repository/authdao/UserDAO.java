package projectpackage.repository.authdao;

import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.model.auth.User;

public interface UserDAO {
    public int insertUser(User user) throws TransactionException;
    public void updateUser(User newUser, User oldUser) throws TransactionException;
}
