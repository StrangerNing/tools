package me.znzn.tools.module.user.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.form.ApiKeyForm;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        ValidatorUtil.validate(loginForm);
        UserInfoVO loginUser = userService.login(loginForm);

        return ResultPageUtil.success(loginUser);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterForm registerForm) {
        ValidatorUtil.validate(registerForm);
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }
        String captcha = registerForm.getCaptchaCode();
        Boolean captchaValid = RecaptchaValidUtil.valid(captcha);
        if (!captchaValid) {
            throw new BusinessException("人机校验未通过");
        }
        return ResultPageUtil.success(userService.register(registerForm));
    }

    @GetMapping("/info")
    public ResponseEntity getUserInfo() {
        return ResultPageUtil.success(LoginUserUtil.getLoginUserMap());
    }

    @PostMapping("/info/update")
    public ResponseEntity updateUserInfo(@RequestBody UserInfoVO userInfoVO) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        if (userInfoVO.getId() == null) {
            throw new BusinessException("用户id为空，请刷新重试");
        }
        Boolean result = userService.update(userInfoVO, user);
        if (result) {
            LoginUserUtil.setSessionUser(userInfoVO);
        }
        return ResultPageUtil.success(result);
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        LoginUserUtil.logout();
        return ResultPageUtil.success();
    }

    @GetMapping("/api/list")
    public ResponseEntity getApiList() {
        return ResultPageUtil.success(userService.getApiKeyList(LoginUserUtil.getUserId()));
    }

    @PostMapping("/api/create")
    public ResponseEntity getApiKey(@RequestBody ApiKeyForm apiKey) {
        apiKey.setCreateId(LoginUserUtil.getUserId());
        return ResultPageUtil.success(userService.getApiKey(apiKey));
    }

    @GetMapping("/api/del/{id}")
    public ResponseEntity delApiKey(@PathVariable Long id) {
        return ResultPageUtil.success(userService.delApiKey(id, LoginUserUtil.getUserId()));
    }

    @PostMapping("/api/update")
    public ResponseEntity updateApiKey(@RequestBody ApiKeyForm apiKeyForm) {
        return ResultPageUtil.success(userService.updateApiKey(apiKeyForm, LoginUserUtil.getUserId()));
    }

    @GetMapping("/api/search")
    public ResponseEntity searchByKey(String ak) {
        return ResultPageUtil.success(userService.getApiKeyByKey(ak, LoginUserUtil.getUserId()));
    }
}
