package projectpackage.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.orders.Order;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.service.orderservice.ModificationHistoryService;

/**
 * Created by Lenovo on 29.05.2017.
 */
@Aspect
public class ModificationHistoryAspect {

    @Autowired
    ModificationHistoryService modificationHistoryService;

    @Around("execution(* projectpackage.repository.ordersdao.OrderDAOImpl.updateOrder(..))")
    public void logAroundUpdateOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        Order newOrder = null;
        Order oldOrder = null;
        for (Object object : joinPoint.getArgs()) {
            System.out.println((Order) object);
            if (null == newOrder) {
                newOrder = (Order) object;
            } else {
                oldOrder = (Order) object;
            }
        }
        boolean orderUpdateGoneSuccessful = true;
        try {
            joinPoint.proceed();
        } catch (TransactionException e) {
            orderUpdateGoneSuccessful = false;
        }
        if (orderUpdateGoneSuccessful) {
            modificationHistoryService.insertModificationHistory(newOrder, oldOrder);
        }
    }
}
