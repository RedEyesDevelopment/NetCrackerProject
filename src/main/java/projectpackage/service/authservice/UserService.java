package projectpackage.service.authservice;

import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();
    public User getSingleUserById(int id);
    public IUDAnswer deleteUser(int id);
    public IUDAnswer insertUser(User user);
    public IUDAnswer updateUser(int id, User newUser);
}
