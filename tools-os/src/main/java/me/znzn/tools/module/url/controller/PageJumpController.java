package me.znzn.tools.module.url.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhuzening
 * @date 2019/11/15
 * @since 1.0
 */
@Controller
public class PageJumpController {

    @RequestMapping("/shortUrl")
    public String shortUrl() {
        return "shortUrl";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/buttons")
    public String buttons() {
        return "/pages/ui-features/buttons";
    }

    @RequestMapping("/test")
    public String test() {
        return "/pages/public/navi";
    }
}
