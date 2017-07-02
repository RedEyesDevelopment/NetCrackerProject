package projectpackage.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.orders.Order;
import projectpackage.service.orderservice.OrderService;

/**
 * Created by Lenovo on 29.05.2017.
 */
@Aspect
public class OrderIsPaidForAspect {

    @Autowired
    OrderService orderService;

    @AfterReturning(pointcut = "execution(* projectpackage.service.orderservice.OrderServiceImpl.insertOrder(..))", returning = "result")
    public void newOrderSetIsPaidFor(JoinPoint joinPoint, Object result) throws Throwable {
        IUDAnswer answer = (IUDAnswer) result;
        Integer orderId = answer.getObjectId();
        if (null!=orderId){
            Order order = orderService.getSingleOrderById(orderId);
            order.setIsPaidFor(true);
            orderService.updateOrder(orderId, order);
        }
    }
}
