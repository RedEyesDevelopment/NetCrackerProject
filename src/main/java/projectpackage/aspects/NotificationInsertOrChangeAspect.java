package projectpackage.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.notifications.Notification;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.service.adminservice.InMemoryNotifService;

/**
 * Created by Lenovo on 07.06.2017.
 */
@Aspect
public class NotificationInsertOrChangeAspect {

    @Autowired
    InMemoryNotifService inMemoryNotifService;

    @Around("execution(* projectpackage.repository.notificationsdao.NotificationDAOImpl.insertNotification(..))")
    public void insertNotification(ProceedingJoinPoint joinPoint) throws Throwable {
        Notification newNot = null;
        for (Object object : joinPoint.getArgs()) {
            if (null == newNot) {
                newNot = (Notification) object;
            }
        }
        boolean notificationInsertGoneSuccessful = true;
        try {
            joinPoint.proceed();
        } catch (TransactionException e) {
            notificationInsertGoneSuccessful = false;
        }
        if (notificationInsertGoneSuccessful) {
            inMemoryNotifService.insertNewNotification(newNot);
        }
    }

    @Around("execution(* projectpackage.repository.notificationsdao.NotificationDAOImpl.updateNotification(..))")
    public void updateNotification(ProceedingJoinPoint joinPoint) throws Throwable {
        Notification newNot = null;
        Notification oldNot = null;
        for (Object object : joinPoint.getArgs()) {
            if (null == newNot) {
                newNot = (Notification) object;
            } else {
                oldNot = (Notification) object;
            }
        }
        boolean notificationUpdateGoneSuccessful = true;
        try {
            joinPoint.proceed();
        } catch (TransactionException e) {
            notificationUpdateGoneSuccessful = false;
        }
        if (notificationUpdateGoneSuccessful && null!=newNot.getExecutedDate()) {
            inMemoryNotifService.removeOldNotification(oldNot);
        }
    }
}
