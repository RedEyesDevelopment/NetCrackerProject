package projectpackage.model.auth;

import lombok.Data;

import java.util.Set;

@Data
public class User {
    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String additionalInfo;
    private Set<Phone> phones;
}
