package me.znzn.tools.module.blog.controller;



import me.znzn.tools.module.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 */
@Controller
@RequestMapping("/markdown/")
public class MarkdownController {

    @Autowired
    private MarkdownService markdownService;

}
