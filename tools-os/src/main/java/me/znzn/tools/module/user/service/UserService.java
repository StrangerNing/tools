package me.znzn.tools.module.user.service;

import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */

public interface UserService {

    /**
     * 用户登陆
     * @param loginForm 登陆项
     * @return 用户信息
     */
    UserInfoVO login(LoginForm loginForm);

    /**
     * 用户注册
     * @param registerForm 注册项
     * @return 注册结果
     */
    Boolean register(RegisterForm registerForm);
}
