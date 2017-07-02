package projectpackage.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.notifications.Notification;
import projectpackage.model.orders.Order;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.orderservice.ModificationHistoryService;

import java.util.Date;

/**
 * Created by Lenovo on 29.05.2017.
 */
@Aspect
public class ModificationHistoryAspect {

    @Autowired
    ModificationHistoryService modificationHistoryService;

    @Autowired
    NotificationService notificationService;

    @Around("execution(* projectpackage.repository.ordersdao.OrderDAOImpl.updateOrder(..))")
    public void logAroundUpdateOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        Order newOrder = null;
        Order oldOrder = null;
        for (Object object : joinPoint.getArgs()) {
            if (null == newOrder) {
                newOrder = (Order) object;
            } else {
                oldOrder = (Order) object;
            }
        }
        boolean orderUpdateGoneSuccessful = true;
        boolean createConfirmNotification =false;
        if (null!=oldOrder && null!=newOrder && null!= oldOrder.getIsPaidFor() && null!=newOrder.getIsPaidFor() && !oldOrder.getIsPaidFor() && newOrder.getIsPaidFor()){
            createConfirmNotification = true;
        }
        try {
            joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            orderUpdateGoneSuccessful = false;
        }
        if (orderUpdateGoneSuccessful) {
            modificationHistoryService.insertModificationHistory(newOrder, oldOrder);
        }
        if (createConfirmNotification) {
            Notification notification = new Notification();
            notification.setAuthor(newOrder.getClient());
            notification.setOrder(newOrder);
            notification.setMessage("The client has paid for the order, please check bank account and confirm the order payment.");
            notification.setSendDate(new Date(System.currentTimeMillis()));

            //TODO: ВСТАВИТЬ НОТИФИКЕЙШН ТАЙП, предварительно создать его в sql-файлике.
//            notification.setNotificationType();
            notificationService.insertNotification(notification);
        }
    }
}
