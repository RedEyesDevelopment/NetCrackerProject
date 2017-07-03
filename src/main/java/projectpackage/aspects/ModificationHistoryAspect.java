package projectpackage.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Order;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.notificationservice.NotificationTypeService;
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

    @Autowired
    NotificationTypeService notificationTypeService;

    @Value("${notification.confirmtext}")
    private String notifText;

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
        if (null!=oldOrder && null!= oldOrder.getIsPaidFor() && null!=newOrder.getIsPaidFor() && !oldOrder.getIsPaidFor() && newOrder.getIsPaidFor()){
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
            notification.setMessage(notifText);
            notification.setSendDate(new Date(System.currentTimeMillis()));
            NotificationType type = notificationTypeService.getSingleNotificationTypeById(23);
            notification.setNotificationType(type);
            notificationService.insertNotification(notification);
        }
    }
}
