package projectpackage.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String additionalInfo;
}
