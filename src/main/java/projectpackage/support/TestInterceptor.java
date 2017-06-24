package projectpackage.support;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import projectpackage.dto.OrderDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Gvozd on 11.12.2016.
 */
public class TestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        HttpSession session = request.getSession();
        System.out.println("**************************************************************************");
        if (null!=session) {
            User thisUser = (User) session.getAttribute("USER");
            System.out.println("SESSION = " + session.toString());
            System.out.println("USER DATA = " + thisUser);
            List<OrderDTO> orderdatra = (List<OrderDTO>) session.getAttribute("ORDERDATA");
            if (null != orderdatra) {
                System.out.println("ORDER DTO DATA = " + orderdatra);
            } else System.out.println("ORDER DTO DATA IS NULL");
            Order order = (Order) session.getAttribute("NEWORDER");
            if (null != order) {
                System.out.println("BOOKED ORDER = " + order);
            } else System.out.println("BOOKED ORDER IS NULL");
        } else System.out.println("SESSION IS NULL");


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
