package me.znzn.tools.module.blog.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.service.CategoryService;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.module.blog.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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

    @Resource
    private FeBlogService feBlogService;
    @Resource
    private TagService tagService;
    @Resource
    private CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model) {
        List<String> navi = new ArrayList<>();
        navi.add("Lifestyle");
        navi.add("IT");
        navi.add("Cook");
        navi.add("Coucou");
        model.addAttribute("navis", navi);

        ArticleForm querySticky = new ArticleForm();
        querySticky.setIsSticky(true);
        List<ArticleVo> stickyArticles = feBlogService.getArticleList(querySticky);
        model.addAttribute("stickies", stickyArticles);

        ArticleForm queryLatest = new ArticleForm();
        queryLatest.setCurrentPage(1);
        queryLatest.setLimit(6);
        queryLatest.setIsSticky(false);
        model.addAttribute("lasts", feBlogService.getArticleList(queryLatest));
        model.addAttribute("lastsCount", feBlogService.countArticleList(queryLatest));
        model.addAttribute("hotTags", tagService.hotTags(5));

        return "index";
    }

    @GetMapping("/wapi/list")
    @ResponseBody
    public ResponseEntity blogList(ArticleForm articleForm) {
        return ResultPageUtil.successWithPage(feBlogService.getArticleList(articleForm), feBlogService.countArticleList(articleForm), articleForm.getCurrentPage());
    }

    @RequestMapping("/404")
    public String notFound(Model model) {
        List<String> navi = new ArrayList<>();
        navi.add("Lifestyle");
        navi.add("IT");
        navi.add("Cook");
        navi.add("Coucou");
        model.addAttribute("navis", navi);

        return "/error/404";
    }
}
