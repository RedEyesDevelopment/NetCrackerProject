package projectpackage.service;

import projectpackage.model.auth.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers(String orderingParameter, boolean ascend);
    public User getSingleUserById(int id);
    public int deleteUserById(int id);
    public boolean insertUser(User user);
    public boolean updateUser(User newUser);
}
