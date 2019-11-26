package me.znzn.tools.module.user.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.LoginUserUtil;
import me.znzn.tools.utils.ValidatorUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.HashMap;

import static me.znzn.tools.common.constant.LoginMap.LOGIN_USER;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/login")
    public Result login(LoginForm loginForm) {
        ValidatorUtil.validate(loginForm);
        return Result.success(userService.login(loginForm));
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterForm registerForm) {
        ValidatorUtil.validate(registerForm);
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }
        return Result.success(userService.register(registerForm));
    }

    @GetMapping("info")
    public Result getUserInfo() {
        return Result.success(LoginUserUtil.getLoginUser());
    }

    @PostMapping("logout")
    public Result logout() {
        String token = LoginUserUtil.getToken();
        LoginUserUtil.delLoginUser(token);
        return Result.success();
    }
}
