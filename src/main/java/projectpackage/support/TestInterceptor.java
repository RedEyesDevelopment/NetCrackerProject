package projectpackage.support;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import projectpackage.dto.OrderDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Gvozd on 11.12.2016.
 */
public class TestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        User thisUser = (User) httpServletRequest.getSession().getAttribute("USER");
        System.out.println("**************************************************************************");
        System.out.println("SESSION = "+httpServletRequest.getSession().toString());
        System.out.println("USER DATA = "+thisUser);
        List<OrderDTO> orderdatra = (List<OrderDTO>) httpServletRequest.getSession().getAttribute("ORDERDATA");
        if (null!=orderdatra) {
            System.out.println("ORDER DTO DATA = "+orderdatra);
        } else System.out.println("ORDER DTO DATA IS NULL");
        Order order = (Order) httpServletRequest.getSession().getAttribute("NEWORDER");
        if (null!=order){
            System.out.println("BOOKED ORDER = "+order);
        } else System.out.println("BOOKED ORDER IS NULL");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
