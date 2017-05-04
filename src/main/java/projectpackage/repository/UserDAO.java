package projectpackage.repository;

import projectpackage.model.auth.User;

import java.util.Map;

public interface UserDAO {
    public Map<Integer,User> getAllUsers(String orderParameter);
}
