package projectpackage.service.support;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Arizel on 21.06.2017.
 */
@Service
public class ServiceUtils {
    private static final Logger LOGGER = Logger.getLogger(ServiceUtils.class);

    protected boolean checkDates(Date startDate, Date finishDate) {
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
}
