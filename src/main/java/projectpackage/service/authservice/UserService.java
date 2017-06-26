package projectpackage.service.authservice;

import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface UserService extends MessageBook{
    public List<User> getAllUsers();
    public User getSingleUserById(Integer id);
    public IUDAnswer deleteUser(Integer id);
    public IUDAnswer insertUser(User user);
    public IUDAnswer updateUser(Integer id, User newUser);
}
