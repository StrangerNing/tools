package me.znzn.tools.module.sso.controller;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.po.Eid;
import me.znzn.tools.module.blog.service.SubscribeService;
import me.znzn.tools.module.user.entity.enums.UserStatusEnum;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.GoogleIdTokenVerifyUtil;
import me.znzn.tools.utils.LoginUserUtil;
import me.znzn.tools.utils.RecaptchaValidUtil;
import me.znzn.tools.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/15
 */
@RequestMapping("/sso")
@Controller
public class SsoController {

    @Resource
    private UserService userService;
    @Resource
    private SubscribeService subscribeService;

    @GetMapping("")
    public Object login(RedirectAttributes redirectAttributes,
                        @RequestParam(name = "redirect", required = false) String redirect,
                        String login,
                        @RequestParam(name = "forceLogin", required = false) String forceLogin) {
        Map loginUser = LoginUserUtil.getLoginUserMap();
        if (StringUtils.isEmpty(redirect)) {
            redirect = CommonConstant.BACKGROUND_DOMAIN;
        }
        if (loginUser != null) {
            RedirectView redirectView = new RedirectView();
            redirect(redirectAttributes, redirectView, redirect, login, (String)loginUser.get("token"));
            return redirectView;
        }

        if (StringUtils.isNotEmpty(forceLogin) && "false".equals(forceLogin)) {
            return new ModelAndView("redirect:" + redirect);
        }
        ModelAndView modelAndView = new ModelAndView("page-login");
        modelAndView.addObject("redirect", redirect);
        modelAndView.addObject("login", login);
        return modelAndView;
    }

    @PostMapping("/login/submit")
    public Object login(RedirectAttributes redirectAttributes, LoginForm loginForm,
                              HttpServletResponse response,
                              @RequestParam(name = "redirect", required = false) String redirect,
                              @RequestParam(name = "login", required = false) String login,
                              Model model) {
        RedirectView redirectView = new RedirectView();
        try {
            redirectView.setContextRelative(false);
            UserInfoVO loginUser = userService.login(loginForm);
            Cookie cookie = new Cookie("user", loginUser.getToken());
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            if (StringUtils.isEmpty(redirect)) {
                redirect = CommonConstant.BACKGROUND_DOMAIN;
            }
            redirect(redirectAttributes, redirectView, redirect, login, loginUser.getToken());
            return redirectView;
        } catch (BusinessException e) {
            model.addAttribute("error", e.getTextMessage());
            return "page-login";
        }
    }

    @GetMapping("/login/google")
    public Object google(RedirectAttributes redirectAttributes, String idToken,
                              HttpServletResponse response,
                              @RequestParam(name = "redirect", required = false) String redirect,
                              @RequestParam(name = "login", required = false) String login,
                              Model model) {
        RedirectView redirectView = new RedirectView();
        try {
            GoogleIdTokenVerifyUtil.GoogleUser googleUser = GoogleIdTokenVerifyUtil.verify(idToken);
            UserInfoVO loginUser = userService.googleLogin(googleUser.getSub());
            if (loginUser == null) {
                loginUser = userService.googleRegister(googleUser);
            }
            redirectView.setContextRelative(false);

            Cookie cookie = new Cookie("user", loginUser.getToken());
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            if (StringUtils.isEmpty(redirect)) {
                redirect = CommonConstant.BACKGROUND_DOMAIN;
            }
            redirect(redirectAttributes, redirectView, redirect, login, loginUser.getToken());
            return redirectView;
        } catch (BusinessException e) {
            model.addAttribute("error", e.getTextMessage());
            return "page-login";
        }
    }

    @GetMapping("/logout")
    public Object logout(String redirect, HttpServletResponse response, Model model) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUserWithoutThrow();
        LoginUserUtil.logout();
        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        if (loginUser.getViaGoogle() != null && loginUser.getViaGoogle() == 1) {
            model.addAttribute("logout", true);
            model.addAttribute("redirect", redirect);
            return "page-login";
        }
        return new ModelAndView("redirect:" + redirect);
    }

    private void redirect(RedirectAttributes redirectAttributes, RedirectView redirectView, String redirect, String login, String token) {
        redirectAttributes.addAttribute("ticket", token);
        if (StringUtils.isNotEmpty(login)) {
            redirectView.setUrl(login);
        } else {
            redirectView.setUrl(redirect);
        }
        if (StringUtils.isNotEmpty(redirect)) {
            redirectAttributes.addAttribute("redirect", redirect);
        }
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("ticket") String ticket, @RequestParam("redirect") String redirect) {
        if (StringUtils.isNotEmpty(ticket)) {
            LoginUserUtil.login(ticket);
        }
        return new ModelAndView("redirect:" + redirect);
    }

    @GetMapping("/register")
    public String register() {
        return "page-register";
    }

    @GetMapping("/confirm")
    public String confirm(Model model, @RequestParam String eid) {
        try {
            Eid eidBean = subscribeService.getEidBeanByEid(eid);
            String email = eidBean.getEmail();
            User user = User.builder().email(email).status(UserStatusEnum.ENABLE.getIndex()).build();
            userService.updateByEmail(user);
        } catch (BusinessException e) {
            model.addAttribute("message", e.getTextMessage());
        }
        return "page-email-confirm";
    }

    @PostMapping("/register")
    @ResponseBody
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
}
