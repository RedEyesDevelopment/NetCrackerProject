package projectpackage.service.support;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Arizel on 21.06.2017.
 */
@Service
public class ServiceUtilsImpl implements ServiceUtils{
    private static final Logger LOGGER = Logger.getLogger(ServiceUtilsImpl.class);

    @Override
    public boolean checkDates(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) return false;

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        Date today = null;
        try {
            today = sdf.parse(sdf.format(new Date().getTime()));
        } catch (ParseException e) {
            LOGGER.error("Exception with parsing date to SimpleDateFormat!", e);
        }

        long maxValidTime = 31536000000L;
        long validStartDate = startDate.getTime() - today.getTime();
        long validFinishDate = finishDate.getTime() - today.getTime();

        if (validStartDate > maxValidTime) return false;
        if (validFinishDate > maxValidTime) return false;
        if (startDate.getTime() < today.getTime()) return false;
        if (finishDate.getTime() < today.getTime()) return false;
        if (startDate.getTime() > finishDate.getTime()) return false;

        return true;
    }

    @Override
    public boolean checkDatesForUpdate(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null) return false;

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        Date today = null;
        try {
            today = sdf.parse(sdf.format(new Date().getTime()));
        } catch (ParseException e) {
            LOGGER.error("Exception with parsing date to SimpleDateFormat!", e);
        }

        long maxValidTime = 31536000000L;
        long validStartDate = startDate.getTime() - today.getTime();
        long validFinishDate = finishDate.getTime() - today.getTime();
        long secondValidStartDate = today.getTime() - maxValidTime;

        if (validStartDate > maxValidTime) return false;
        if (validFinishDate > maxValidTime) return false;
        if (startDate.getTime() < secondValidStartDate) return false;
        if (finishDate.before(today)) return false;
        if (startDate.after(finishDate)) return false;

        return true;
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
    public IUDAnswer checkNoClientSessionData(User user, Object data) {
        return null;
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
