package projectpackage.service.authservice;

import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface UserService extends MessageBook{
    List<User> getAllUsers();
    User getSingleUserById(Integer id);
    User getSingleUserByUsername(String username);
    IUDAnswer deleteUser(Integer id);
    IUDAnswer restoreUser(Integer id);
    IUDAnswer insertUser(User user);
    IUDAnswer updateUser(Integer id, User newUser);
    IUDAnswer updateUserPassword(Integer id, User newUser);
}
