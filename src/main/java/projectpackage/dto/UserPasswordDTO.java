package projectpackage.dto;

import lombok.Data;

/**
 * Created by Arizel on 27.06.2017.
 */
@Data
public class UserPasswordDTO {
    private String oldPassword;
    private String newPassword;
}
