package me.znzn.tools.module.user.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.form.ApiKeyForm;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.LoginUserUtil;
import me.znzn.tools.utils.RecaptchaValidUtil;
import me.znzn.tools.utils.ValidatorUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public Result login(LoginForm loginForm, HttpServletRequest request) {
        ValidatorUtil.validate(loginForm);
        UserInfoVO loginUser = userService.login(loginForm);
        request.getSession().setAttribute("user", loginUser);
        return Result.success(userService.login(loginForm));
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterForm registerForm) {
        ValidatorUtil.validate(registerForm);
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }
        String captcha = registerForm.getCaptchaCode();
        Boolean captchaValid = RecaptchaValidUtil.valid(captcha);
        if (!captchaValid) {
            throw new BusinessException("人机校验未通过");
        }
        return Result.success(userService.register(registerForm));
    }

    @GetMapping("/info")
    public Result getUserInfo(HttpServletRequest request) {
        return Result.success(request.getSession().getAttribute("user"));
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return Result.success();
    }

    @GetMapping("/api/list")
    public Result getApiList() {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return Result.success(userService.getApiKeyList(loginUser.getId()));
    }

    @PostMapping("/api/create")
    public Result getApiKey(@RequestBody ApiKeyForm apiKey) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        apiKey.setCreateId(loginUser.getId());
        return Result.success(userService.getApiKey(apiKey));
    }

    @GetMapping("/api/del/{id}")
    public Result delApiKey(@PathVariable Long id) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return Result.success(userService.delApiKey(id, loginUser.getId()));
    }

    @PostMapping("/api/update")
    public Result updateApiKey(@RequestBody ApiKeyForm apiKeyForm) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return Result.success(userService.updateApiKey(apiKeyForm, loginUser.getId()));
    }

    @GetMapping("/api/search")
    public Result searchByKey(String ak) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return Result.success(userService.getApiKeyByKey(ak, loginUser.getId()));
    }
}
