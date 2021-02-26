package me.znzn.tools.module.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/25
 */
@Controller
public class FeBlogController {

    @GetMapping("/")
    public String index(Model model) {
        List<String> navi = new ArrayList<>();
        navi.add("Lifestyle");
        navi.add("IT");
        navi.add("Cook");
        navi.add("Coucou");
        model.addAttribute("navis", navi);

        return "index";
    }
}
