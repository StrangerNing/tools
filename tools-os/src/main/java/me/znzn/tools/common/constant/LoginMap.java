package me.znzn.tools.common.constant;

import me.znzn.tools.module.user.entity.vo.UserInfoVO;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuzening
 * @date 2019/11/20
 * @since 1.0
 */
public class LoginMap {

    public static ConcurrentHashMap<String, UserInfoVO> LOGIN_USER = new ConcurrentHashMap<>(5000);

}
