package projectpackage.repository.authdao;

import projectpackage.model.auth.User;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface UserDAO extends Commitable, Rollbackable{
    User getUser(Integer id);
    List<User> getAllUsers();
    Integer insertUser(User user);
    Integer updateUser(User newUser, User oldUser);
    Integer updateUserPassword(User newUser, User oldUser);
    void deleteUser(Integer id);
    void restoreUser(Integer id);
    User getUserByUsername(String username);
}
