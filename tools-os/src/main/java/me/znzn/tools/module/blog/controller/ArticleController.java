package me.znzn.tools.module.blog.controller;


import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.service.ArticleService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 */
@Controller
@RequestMapping("/wapi/blog/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody ArticleVo articleVo) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        return ResultPageUtil.success(articleService.addArticle(articleVo, user));
    }

    @PostMapping("/edit")
    public ResponseEntity edit(@RequestBody ArticleVo articleVo) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        return ResultPageUtil.success(articleService.editArticle(articleVo, user));
    }

    @GetMapping("/list")
    public ResponseEntity list(ArticleForm articleForm) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        return ResultPageUtil.successWithPage(articleService.articleList(articleForm, user), articleService.countArticle(articleForm, user), articleForm.getCurrentPage());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        return ResultPageUtil.success(articleService.articleDetail(id));
    }

    @GetMapping("/del/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResultPageUtil.success();
    }

}
