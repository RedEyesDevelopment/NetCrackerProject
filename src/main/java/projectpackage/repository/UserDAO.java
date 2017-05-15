package projectpackage.repository;

import projectpackage.model.auth.User;

import java.util.Map;

public interface UserDAO {
    public Map<Integer,User> getAllUsers(String orderParameter);
    public void insertUser(User user);
    public void updateUser(User user);
}
