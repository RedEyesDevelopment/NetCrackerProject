package projectpackage.repository;

import projectpackage.model.auth.User;

public interface UserDAO {
    public void insertUser(User user);
    public void updateUser(User newUser, User oldUser);
}
