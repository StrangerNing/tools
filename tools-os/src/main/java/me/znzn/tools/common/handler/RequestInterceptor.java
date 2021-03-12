package me.znzn.tools.common.handler;

import me.znzn.tools.utils.LoginUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object isLogin = request.getSession().getAttribute("user");

        if (isLogin == null) {
            request.getSession().setAttribute("user", "false");
            String url ="";
            url = request.getScheme() +"://" + request.getServerName()
                    +":" +request.getServerPort()
                    + request.getServletPath();
            if (request.getQueryString() !=null){
                url +="?" + request.getQueryString();
            }

            response.sendRedirect("https://admin.edchu.cn/#/login?redirect=" + url);
            return false;
        }
        if (!"false".equals(isLogin)) {
            LoginUserUtil.renewLoginInfo(request);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
