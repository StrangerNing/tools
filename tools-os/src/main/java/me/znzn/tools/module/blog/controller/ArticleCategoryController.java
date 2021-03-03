package me.znzn.tools.module.blog.controller;



import me.znzn.tools.module.blog.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理
 *
 * @author 
 * @version 1.0.0
 * @date 2021/02/28 21:33:36
 * @copyright 
 */
@Controller
@RequestMapping("/articleCategory/")
public class ArticleCategoryController {

    @Autowired
    private ArticleCategoryService articleCategoryService;

}