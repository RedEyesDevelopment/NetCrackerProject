package projectpackage.service;

import projectpackage.model.auth.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers(String orderingParameter);
}
