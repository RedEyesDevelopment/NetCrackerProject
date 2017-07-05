package projectpackage.service.support;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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
    private static final long MAX_VALID_TIME = 70036000000L;

    @Override
    public boolean checkDates(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) {
            return false;
        }

        Date today = new Date();

        long validStartDate = startDate.getTime() - today.getTime();
        long validFinishDate = finishDate.getTime() - today.getTime();

        if (validStartDate > MAX_VALID_TIME) {
            return false;
        } else if (validFinishDate > MAX_VALID_TIME) {
            return false;
        } else if (startDate.getTime() < today.getTime()) {
            return false;
        } else if (finishDate.getTime() < today.getTime()) {
            return false;
        } else if (startDate.getTime() > finishDate.getTime()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkDatesForUpdate(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) {
            return false;
        }

        Date today = new Date();

        long validStartDate = startDate.getTime() - today.getTime();
        long validFinishDate = finishDate.getTime() - today.getTime();
        long secondValidStartDate = today.getTime() - MAX_VALID_TIME;

        if (validStartDate > MAX_VALID_TIME) {
            return false;
        }
        if (validFinishDate > MAX_VALID_TIME) {
            return false;
        }
        if (startDate.getTime() < secondValidStartDate) {
            return false;
        }
        if (finishDate.before(today)) {
            return false;
        }
        if (startDate.after(finishDate)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkDatesForRate(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) {
            return false;
        }
        DateTime dateTime = new DateTime();
        if (startDate.getTime() == finishDate.getTime()) {
            return false;
        }
        if (finishDate.before(startDate)) {
            return false;
        }
        if (finishDate.before(dateTime.minusYears(1).toDate())) {
            return false;
        }
        if (startDate.after(dateTime.plusYears(2).toDate())) {
            return false;
        }
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
    public IUDAnswer checkSessionAdminReceptionAndData(User user, Object data) {
        IUDAnswer iudAnswer = checkSessionAndData(user, data);
        return checkForAdminAndReception(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkSessionAdminReceptionAndData(User user, Object data, Integer id) {
        IUDAnswer iudAnswer = checkSessionAndData(user, data, id);
        return checkForAdminAndReception(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkSessionAdminReceptionAndData(User user, Integer id) {
        IUDAnswer iudAnswer = checkSessionAndData(user, new Object(), id);
        return checkForAdminAndReception(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkDeleteForAdmin(User user, Integer id) {
        IUDAnswer iudAnswer = checkDelete(user, id);
        return checkForAdmin(iudAnswer, user);
    }

    @Override
    public IUDAnswer checkDeleteForAdminAndReception(User user, Integer id) {
        IUDAnswer iudAnswer = checkDelete(user, id);
        return checkForAdminAndReception(iudAnswer, user);
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

    @Override
    public IUDAnswer checkAdminForChangePassword(User user, UserPasswordDTO data, Integer id) {
        IUDAnswer iudAnswer = checkForChangePassword(user, data, id);
        return checkForAdmin(iudAnswer, user);
    }

    @Override
    public boolean isClient(User user) {
        if (user == null) {
            return false;
        } else if (user.getRole().getObjectId() != 3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isReception(User user) {
        if (user == null) {
            return false;
        } else if (user.getRole().getObjectId() != 2) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isAdmin(User user) {
        if (user == null) {
            return false;
        } else if (user.getRole().getObjectId() != 1) {
            return false;
        } else {
            return true;
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

    private IUDAnswer checkForAdminAndReception(IUDAnswer iudAnswer, User user) {
        if (!iudAnswer.isSuccessful()) {
            return iudAnswer;
        } else if (user.getRole().getObjectId() == 3) {
            return new IUDAnswer(false, NOT_RECEPTION_OR_ADMIN);
        } else {
            return new IUDAnswer(true);
        }
    }

}
