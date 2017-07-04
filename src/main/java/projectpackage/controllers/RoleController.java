package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.Role;
import projectpackage.service.authservice.RoleService;

import javax.cache.annotation.CacheResult;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "roleList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Role>> getRoleList() {
        List<Role> roles = roleService.getAllRoles();
        List<Resource<Role>> resources = new ArrayList<>(roles.size());

        for (Role role : roles) {
            Resource<Role> resource = new Resource<>(role);
            resources.add(resource);
        }

        return resources;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Role>> getRole(@PathVariable("id") Integer id) {
        Role role = roleService.getSingleRoleById(id);
        Resource<Role> resource = new Resource<>(role);

        HttpStatus status = (null != role) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ResponseEntity<Resource<Role>> response = new ResponseEntity<Resource<Role>>(resource, status);
        return response;
    }
}
