package projectpackage.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.orders.Order;
import projectpackage.service.orderservice.OrderService;

@Log4j
@Aspect
public class OrderIsPaidForAspect {

    @Autowired
    OrderService orderService;

    @AfterReturning(pointcut = "execution(* projectpackage.service.orderservice.OrderServiceImpl.insertOrder(..))", returning = "result")
    public void newOrderSetIsPaidFor(JoinPoint joinPoint, Object result) throws Throwable {
        IUDAnswer answer = (IUDAnswer) result;
        Integer orderId = answer.getObjectId();
        if (null != orderId) {
            Order order = orderService.getSingleOrderById(orderId);
            order.setIsPaidFor(true);
            IUDAnswer updateAnswer = orderService.updateOrder(orderId, order);
            if (log.isInfoEnabled()) {
                log.info("Order automatically set to be already paid for " + updateAnswer);
            }
        }
    }
}
