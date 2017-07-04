package projectpackage.support;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import projectpackage.dto.OrderDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class TestInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = Logger.getLogger(TestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        HttpSession session = request.getSession();
        LOGGER.info("**************************************************************************");
        if (null!=session) {
            User thisUser = (User) session.getAttribute("USER");
            LOGGER.info("SESSION = " + session.toString());
            LOGGER.info("USER DATA = " + thisUser);
            List<OrderDTO> orderdatra = (List<OrderDTO>) session.getAttribute("ORDERDATA");
            if (null != orderdatra) {
                LOGGER.info("ORDER DTO DATA = " + orderdatra);
            } else LOGGER.info("ORDER DTO DATA IS NULL");
            Order order = (Order) session.getAttribute("NEWORDER");
            if (null != order) {
                LOGGER.info("BOOKED ORDER = " + order);
            } else LOGGER.info("BOOKED ORDER IS NULL");
        } else LOGGER.info("SESSION IS NULL");
        LOGGER.info("Object: "+o);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
