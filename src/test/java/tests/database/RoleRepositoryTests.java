package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Role;
import projectpackage.service.authservice.RoleService;

import java.util.List;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional(value = "annotationDrivenTransactionManager")
public class RoleRepositoryTests extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    RoleService roleService;

    @Test
    @Rollback(true)
    public void getSingleRoleByRoleName(String rolename) {

    }

    @Test
    @Rollback(true)
    public void getAllRoles() {
        List<Role> list = roleService.getAllRoles("roleName", true);
        for (Role role:list){
            System.out.println(role);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllRoles(String orderingParameter, boolean ascend) {

    }

    @Test
    @Rollback(true)
    public void getSingleRoleById(){
        Role role = null;
        int roleId = 3;
        role = roleService.getSingleRoleById(roleId);
        System.out.println(role);
        System.out.println(SEPARATOR);
    }

}