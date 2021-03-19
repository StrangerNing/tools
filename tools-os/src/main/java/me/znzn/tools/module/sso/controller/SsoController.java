package me.znzn.tools.module.sso.controller;

import cn.hutool.core.util.URLUtil;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
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

    @GetMapping("")
    public Object login(RedirectAttributes redirectAttributes,
                        String redirect,
                        String login,
                        @RequestParam(name = "forceLogin", required = false) String forceLogin) {
        Map loginUser = LoginUserUtil.getLoginUserMap();
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
    public RedirectView login(RedirectAttributes redirectAttributes, LoginForm loginForm,
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
            redirect(redirectAttributes, redirectView, redirect, login, loginUser.getToken());
            return redirectView;
        } catch (BusinessException e) {
            redirectView.setUrl("/");
            model.addAttribute("error", e.getTextMessage());
            return redirectView;
        }
    }

    @GetMapping("/logout")
    public Object logout(String redirect, HttpServletResponse response) {
        LoginUserUtil.logout();
        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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
}
