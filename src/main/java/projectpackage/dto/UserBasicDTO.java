package projectpackage.dto;

import lombok.Data;

@Data
public class UserBasicDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String additionalInfo;
}
