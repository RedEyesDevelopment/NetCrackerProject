package projectpackage.service.support;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.UserPasswordDTO;
import projectpackage.model.auth.User;

import java.util.Date;


/**
 * Created by Arizel on 21.06.2017.
 */
@Service
public class ServiceUtilsImpl implements ServiceUtils{
    private static final Logger LOGGER = Logger.getLogger(ServiceUtilsImpl.class);
    private static final long MAX_VALID_TIME = 31536000000L;

    @Override
    public boolean checkDates(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) return false;

        Date today = new Date();

        long validStartDate = startDate.getTime() - today.getTime();
        long validFinishDate = finishDate.getTime() - today.getTime();

        if (validStartDate > MAX_VALID_TIME) return false;
        if (validFinishDate > MAX_VALID_TIME) return false;
        if (startDate.getTime() < today.getTime()) return false;
        if (finishDate.getTime() < today.getTime()) return false;
        if (startDate.getTime() > finishDate.getTime()) return false;

        return true;
    }

    @Override
    public boolean checkDatesForUpdate(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) return false;

        Date today = new Date();

        long validStartDate = startDate.getTime() - today.getTime();
        long validFinishDate = finishDate.getTime() - today.getTime();
        long secondValidStartDate = today.getTime() - MAX_VALID_TIME;

        if (validStartDate > MAX_VALID_TIME) return false;
        if (validFinishDate > MAX_VALID_TIME) return false;
        if (startDate.getTime() < secondValidStartDate) return false;
        if (finishDate.before(today)) return false;
        if (startDate.after(finishDate)) return false;

        return true;
    }

    @Override
    public IUDAnswer checkSessionAdminAndData(User user, Object data) {
        IUDAnswer iudAnswer = checkSessionAndData(user, data);
        return checkForAdmin(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkSessionAdminAndData(User user, Object data, Integer id) {
        IUDAnswer iudAnswer = checkSessionAndData(user, data, id);
        return checkForAdmin(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkDeleteForAdmin(User user, Integer id) {
        IUDAnswer iudAnswer = checkDelete(user, id);
        return checkForAdmin(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkSessionAndData(User user, Object data) {
        if (user == null) {
            return new IUDAnswer(false, NEED_TO_AUTH);
        } else if (data == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }

        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer checkSessionAndData(User user, Object data, Integer id) {
        IUDAnswer iudAnswer = checkSessionAndData(user, data);
        if (!iudAnswer.isSuccessful()) {
            return iudAnswer;
        } else if (id == null) {
            return new IUDAnswer(id, false, NULL_ID);
        } else {
            return new IUDAnswer(true);
        }
    }

    @Override
    public IUDAnswer checkDelete(User user, Integer id) {
        if (user == null) {
            return new IUDAnswer(false, NEED_TO_AUTH);
        } else if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        } else {
            return new IUDAnswer(true);
        }
    }

    @Override
    public IUDAnswer checkForChangePassword(User user, UserPasswordDTO data, Integer id) {
        IUDAnswer iudAnswer = checkSessionAndData(user, data, id);
        if (!iudAnswer.isSuccessful()) {
            return iudAnswer;
        } else if ((data.getNewPassword() == null) || (data.getOldPassword() == null) || data.getNewPassword().isEmpty()
                || data.getOldPassword().isEmpty()) {
            return new IUDAnswer(false, WRONG_FIELD);
        } else if (data.getOldPassword().equals(data.getNewPassword())) {
            return new IUDAnswer(false, SAME_PASSWORDS);
        } else {
            return new IUDAnswer(true);
        }
    }

    private IUDAnswer checkForAdmin(IUDAnswer iudAnswer, User user) {
        if (!iudAnswer.isSuccessful()) {
            return iudAnswer;
        } else if (user.getRole().getObjectId() != 1) {
            return new IUDAnswer(false, NOT_ADMIN);
        } else {
            return new IUDAnswer(true);
        }
    }

}
