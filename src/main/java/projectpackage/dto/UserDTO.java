package projectpackage.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String additionalInfo;
    private List<String> phones;
}
