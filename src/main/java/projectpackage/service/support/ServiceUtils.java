package projectpackage.service.support;

import projectpackage.dto.IUDAnswer;
import projectpackage.dto.UserPasswordDTO;
import projectpackage.model.auth.User;
import projectpackage.service.MessageBook;

import java.util.Date;

/**
 * Created by Arizel on 27.06.2017.
 */
public interface ServiceUtils extends MessageBook{
    boolean checkDates(Date startDate, Date finishDate);
    boolean checkDatesForUpdate(Date startDate, Date finishDate);
    IUDAnswer checkSessionAndData(User user, Object data, Integer id);
    IUDAnswer checkSessionAndData(User user, Object data);
    IUDAnswer checkSessionAdminAndData(User user, Object data);
    IUDAnswer checkSessionAdminAndData(User user, Object data, Integer id);
    IUDAnswer checkSessionAdminReceptionAndData(User user, Object data);
    IUDAnswer checkForChangePassword(User user, UserPasswordDTO data, Integer id);
    IUDAnswer checkDeleteForAdmin(User user, Integer id);
    IUDAnswer checkDeleteForAdminAndReception(User user, Integer id);
    IUDAnswer checkDelete(User user, Integer id);
}
