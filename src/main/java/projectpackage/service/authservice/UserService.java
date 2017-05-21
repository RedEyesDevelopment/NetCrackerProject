package projectpackage.service.authservice;

import projectpackage.model.auth.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();
    public User getSingleUserById(int id);
    public boolean deleteUser(int id);
    public boolean insertUser(User user);
    public boolean updateUser(int id, User newUser);
}
