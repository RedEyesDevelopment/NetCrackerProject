package projectpackage.service;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.PhoneDAO;
import projectpackage.repository.UserDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    PhoneDAO phoneDAO;


    @Override
    public List<User> getAllUsers(String orderingParameter) {
        Map<Integer,User> users = userDAO.getAllUsers(orderingParameter);
        List<Phone> phones = phoneDAO.getPhonesList();
        for (Phone phone:phones){
            User user = users.get(phone.getUserId());
            if (null!=user){
                if (null==user.getPhones()){
                    user.setPhones(new HashSet<Phone>());
                }
                user.getPhones().add(phone);
            }
        }
        return ImmutableList.copyOf(users.values());
    }

}
