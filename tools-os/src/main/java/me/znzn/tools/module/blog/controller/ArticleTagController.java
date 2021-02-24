package me.znzn.tools.module.blog.controller;



import me.znzn.tools.module.blog.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/22 15:23:02
 */
@Controller
@RequestMapping("/articleTag/")
public class ArticleTagController {

    @Autowired
    private ArticleTagService articleTagService;

}
