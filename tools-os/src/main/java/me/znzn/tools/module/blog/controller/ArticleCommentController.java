package me.znzn.tools.module.blog.controller;



import me.znzn.tools.module.blog.service.ArticleCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理
 *
 * @author 
 * @version 1.0.0
 * @date 2021/03/07 23:43:35
 * @copyright 
 */
@Controller
@RequestMapping("/articleComment/")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentService articleCommentService;

}