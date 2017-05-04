//package projectpackage.repository.rowMappers;
//
//import org.springframework.jdbc.core.RowMapper;
//import projectpackage.model.auth.Role;
//import projectpackage.model.auth.User;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * Created by Lenovo on 04.05.2017.
// */
//public class UserRowMapper implements RowMapper<User> {
//    @Override
//    public User mapRow(ResultSet resultSet, int i) throws SQLException {
//        User user = new User();
//        Role role = new Role();
//        role.setRoleName(resultSet.getString("ROLE"));
//        return null;
//    }
//}
