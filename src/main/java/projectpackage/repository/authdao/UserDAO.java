package projectpackage.repository.authdao;

import projectpackage.model.auth.User;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface UserDAO extends Commitable, Rollbackable{
    public User getUser(Integer id);
    public List<User> getAllUsers();
    public Integer insertUser(User user);
    public Integer updateUser(User newUser, User oldUser);
    public Integer updateUserPassword(User newUser, User oldUser);
    public void deleteUser(Integer id);
    User getUserByUsername(String username);
}
