package me.znzn.tools.utils;

import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static me.znzn.tools.common.constant.LoginMap.LOGIN_USER;

/**
 * @author zhuzening
 * @date 2019/11/20
 * @since 1.0
 */
public class LoginUserUtil {

    public static UserInfoVO getLoginUser() {
        String token = getToken();
        UserInfoVO loginUser = LOGIN_USER.get(token);
        if (null == loginUser) {
            throw new BusinessException("401","登陆状态失效，请重新登陆");
        }
        return loginUser;
    }

    public static UserInfoVO getLoginUser(String token) {
        return LOGIN_USER.get(token);
    }

    public static void delLoginUser(String token) {
        if (!StringUtils.isEmpty(token)) {
            LOGIN_USER.remove(token);
        }
    }

    public static String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("未找到token");
        }
        return token;
    }

    public static UserInfoVO getSessionUser(HttpServletRequest request) {
        UserInfoVO loginUser = (UserInfoVO)request.getSession().getAttribute("user");
        if (null == loginUser) {
            throw new BusinessException("401", "用户未登陆，请先登陆");
        }
        return loginUser;
    }
}
