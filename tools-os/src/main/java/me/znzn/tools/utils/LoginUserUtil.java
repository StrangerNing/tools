package me.znzn.tools.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuzening
 * @date 2019/11/20
 * @since 1.0
 */
public class LoginUserUtil {

    public static UserInfoVO getSessionUser() {
        UserInfoVO loginUser = getSessionUserWithoutThrow();
        if (null == loginUser) {
            throw new BusinessException("401", "用户未登陆，请先登陆");
        }
        return loginUser;
    }

    public static Map getLoginUserMap() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        Map user = (Map)request.getSession().getAttribute("user");
        if (user != null) {
            return user;
        }
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("user".equals(cookie.getName())) {
                RedisTemplate redisTemplate = (RedisTemplate)SpringUtil.getBean("redisTemplate");
                if (redisTemplate.hasKey(cookie.getValue())) {
                    Map cacheUser = redisTemplate.opsForHash().entries(cookie.getValue());
                    request.getSession().setAttribute("user", cacheUser);
                }
            }
        }
        return null;
    }

    public static UserInfoVO getSessionUserWithoutThrow() {
        return BeanUtil.mapToBean(getLoginUserMap(), UserInfoVO.class, true, CopyOptions.create());
    }

    public static void setSessionUser(UserInfoVO loginUser) {
        RedisTemplate redisTemplate = (RedisTemplate)SpringUtil.getBean("redisTemplate");
        redisTemplate.opsForHash().delete(loginUser.getToken());
        redisTemplate.opsForHash().putAll(loginUser.getToken(), BeanUtil.beanToMap(loginUser));
    }

    public static void login(UserInfoVO userInfoVO) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        RedisTemplate redisTemplate = (RedisTemplate)SpringUtil.getBean("redisTemplate");
        Map user = BeanUtil.beanToMap(userInfoVO);
        redisTemplate.opsForHash().putAll(userInfoVO.getToken(), user);
        redisTemplate.expire(userInfoVO.getToken(), 3, TimeUnit.HOURS);
        request.getSession().setAttribute("user", user);
    }

    public static void logout() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("user".equals(cookie.getName())) {
                RedisTemplate redisTemplate = (RedisTemplate)SpringUtil.getBean("redisTemplate");
                redisTemplate.opsForHash().delete(cookie.getValue());
            }
        }
    }

    public static Long getUserId() {
        Map user = getLoginUserMap();
        if (user == null) {
            throw new BusinessException("401", "用户未登陆，请先登陆");
        }
        return (Long)user.get("id");
    }

    public static void renewLoginInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if ("user".equals(cookie.getName())) {
                RedisTemplate redisTemplate = (RedisTemplate)SpringUtil.getBean("redisTemplate");
                if (redisTemplate.hasKey(cookie.getValue())) {
                    redisTemplate.expire(cookie.getValue(), 3, TimeUnit.HOURS);
                }
            }
        }
    }
}
