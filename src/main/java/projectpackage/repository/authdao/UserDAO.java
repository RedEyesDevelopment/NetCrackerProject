package projectpackage.repository.authdao;

import projectpackage.repository.reacdao.exceptions.TransactionException;
import projectpackage.model.auth.User;

public interface UserDAO {
    public void insertUser(User user) throws TransactionException;
    public void updateUser(User newUser, User oldUser) throws TransactionException;
}
