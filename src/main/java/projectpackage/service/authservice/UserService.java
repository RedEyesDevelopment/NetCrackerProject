package projectpackage.service.authservice;

import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;

import java.util.List;

public interface UserService {
    public List<User> getUsersByRole(Role role);//TODO Denis

    public List<User> getAllUsers();//TODO Merlyan
    public List<User> getAllUsers(String orderingParameter, boolean ascend);
    public User getSingleUserById(int id);
    public boolean deleteUser(int id);
    public boolean insertUser(User user);
    public boolean updateUser(int id, User newUser);
}
