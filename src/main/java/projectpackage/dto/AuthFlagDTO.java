package projectpackage.dto;

import lombok.Data;

@Data
public class AuthFlagDTO {
    private String rolename;

    public AuthFlagDTO(String rolename) {
        this.rolename = rolename;
    }
}
