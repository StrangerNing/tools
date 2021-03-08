package me.znzn.tools.module.blog.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.exception.NotFoundException;
import me.znzn.tools.module.blog.entity.enums.ArticleStatusEnum;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.form.CategoryForm;
import me.znzn.tools.module.blog.entity.po.ArticleComment;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.service.CategoryService;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.module.blog.service.TagService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

        ArticleForm querySticky = new ArticleForm();
        querySticky.setIsSticky(true);
        querySticky.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        querySticky.setOrderBy("article.priority DESC, article.id DESC");
        List<ArticleVo> stickyArticles = feBlogService.getArticleList(querySticky);
        model.addAttribute("stickies", stickyArticles);

        ArticleForm queryLatest = new ArticleForm();
        queryLatest.setCurrentPage(1);
        queryLatest.setLimit(6);
        queryLatest.setIsSticky(false);
        queryLatest.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        queryLatest.setOrderBy("article.priority DESC, article.id DESC");
        model.addAttribute("lasts", feBlogService.getArticleList(queryLatest));
        model.addAttribute("lastsCount", feBlogService.countArticleList(queryLatest).getTotalCount());
        model.addAttribute("hotTags", tagService.hotTags(10));
        model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        return "index";
    }

    @GetMapping("/wapi/list")
    @ResponseBody
    public ResponseEntity blogList(ArticleForm articleForm) {
        articleForm.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        return ResultPageUtil.successWithPage(feBlogService.getArticleList(articleForm), feBlogService.countArticleList(articleForm).getTotalCount(), articleForm.getCurrentPage());
    }

    @PostMapping("/wapi/comment/add")
    @ResponseBody
    public ResponseEntity addComment(@RequestBody ArticleComment articleComment) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUserWithoutThrow();
        feBlogService.addArticleComment(articleComment, userInfoVO);
        return ResultPageUtil.success();
    }

    @GetMapping(value = {"/category/{category}","/category/{category}/{currentPage}"})
    public String category(@PathVariable String category,@PathVariable(required = false) Integer currentPage, Model model) {
        try {
            Category query = new Category();
            query.setHref(category);
            query = categoryService.getOneCategory(query);

            ArticleForm articleForm = new ArticleForm();
            articleForm.setCategory(query.getName());
            List<ArticleVo> articles = getArticles(model, articleForm, currentPage);
            model.addAttribute("name", query.getName());
            model.addAttribute("href", "/category/" + category + "/");

            getHotArticles(model, articles);
            return "category-list";
        } catch (NotFoundException ntf) {
            return "error/404";
        } finally {
            model.addAttribute("hotTags", tagService.hotTags(10));
        }

    }

    @GetMapping(value = {"/tag/{tag}","/tag/{tag}/{currentPage}"})
    public String tag(@PathVariable String tag, @PathVariable(required = false) Integer currentPage, Model model) {
        try {
            Tag query = new Tag();
            query.setTag(tag);
            query = tagService.getOneTag(query);

            ArticleForm articleForm = new ArticleForm();
            articleForm.setTag(query.getTag());
            List<ArticleVo> articles = getArticles(model, articleForm, currentPage);
            model.addAttribute("name", tag);
            model.addAttribute("href", "/tag/" + tag + "/");

            getHotArticles(model, articles);
            return "category-list";
        } catch (NotFoundException ntf) {
            return "error/404";
        } finally {
            model.addAttribute("hotTags", tagService.hotTags(10));
        }
    }

    @GetMapping("/posts/{alias}")
    public String posts(@PathVariable String alias, Model model) {
        try {
            ArticleVo article = feBlogService.getOneArticle(alias);
            model.addAttribute("article", article);
            feBlogService.addViews(article.getId());

            model.addAttribute("comments", feBlogService.getComments(article.getId()));
            model.addAttribute("recommends", feBlogService.getRecommendArticle(article.getId()));
            return "single";
        } catch (NotFoundException ntf) {
            return "error/404";
        } finally {
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
            model.addAttribute("hotTags", tagService.hotTags(10));
        }
    }

    @RequestMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("hotTags", tagService.hotTags(10));
        return "/error/404";
    }

    private List<ArticleVo> getArticles(Model model, ArticleForm articleForm, Integer currentPage) {
        articleForm.setCurrentPage(currentPage == null ? 1 : currentPage);
        articleForm.setLimit(5);
        articleForm.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        articleForm.setOrderBy("article.priority DESC, article.id DESC");
        List<ArticleVo> articles = feBlogService.getArticleList(articleForm);
        model.addAttribute("articles", articles);
        model.addAttribute("pageInfo", feBlogService.countArticleList(articleForm));
        return articles;
    }

    private void getHotArticles(Model model, List<ArticleVo> articles) {
        if (CollectionUtil.isNotEmpty(articles) && articles.size() > 2) {
            ArticleForm hot = new ArticleForm();
            hot.setCurrentPage(1);
            hot.setLimit(articles.size() - 1);
            hot.setStatus(ArticleStatusEnum.NORMAL.getIndex());
            hot.setOrderBy("article.views DESC, article.id DESC");
            List<ArticleVo> hotArticles = feBlogService.getArticleList(hot);
            model.addAttribute("hotArticles", hotArticles);
        }
    }
}
