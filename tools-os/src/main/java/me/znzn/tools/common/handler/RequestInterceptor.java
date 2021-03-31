package me.znzn.tools.common.handler;

import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Object isLogin = request.getSession().getAttribute("isLogin");
//        String dontLogin = request.getParameter("login");
//        if (StringUtils.isNotEmpty(dontLogin)) {
//            return true;
//        }
        String ticket = request.getParameter("ticket");
        if (StringUtils.isNotEmpty(ticket)) {
            LoginUserUtil.login(ticket);
        }
//        if (isLogin != null) {
//            return true;
//        }
//        Map user = LoginUserUtil.getLoginUserMap();
//        if (user == null) {
//            request.getSession().setAttribute("isLogin", 1);
//            String url ="";
//            url =  "https://" + request.getServerName()
//                    + request.getServletPath();
//            if (request.getQueryString() !=null){
//                url +="?" + request.getQueryString();
//            }
//            response.sendRedirect(CommonConstant.SSO_URL + "?forceLogin=false&redirect="+url);
//        }
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
