package me.znzn.tools.module.blog.controller;


import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 管理
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/28 21:33:12
 * @copyright
 */
@Controller
@RequestMapping("/blog/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/search")
    public ResponseEntity search(Category category) {
        return ResultPageUtil.success(categoryService.searchCategory(category));
    }

}
