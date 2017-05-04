package projectpackage.model.auth;

import lombok.Data;

import java.util.Set;

@Data
public class User {
    private int objectId;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String additionalInfo;
    private Set<Phone> phones;
}
