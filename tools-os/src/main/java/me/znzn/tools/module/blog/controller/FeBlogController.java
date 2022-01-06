package me.znzn.tools.module.blog.controller;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.Page;
import me.znzn.tools.common.component.ResultListData;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.common.exception.NotFoundException;
import me.znzn.tools.module.blog.entity.enums.ArticleStatusEnum;
import me.znzn.tools.module.blog.entity.enums.ArticleTypeEnum;
import me.znzn.tools.module.blog.entity.enums.FriendsLinkStatusEnum;
import me.znzn.tools.module.blog.entity.enums.PageTypeEnum;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.form.CategoryForm;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.entity.po.Friends;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.service.*;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/25
 */
@Slf4j
@Controller
public class FeBlogController {

    @Resource
    private FeBlogService feBlogService;
    @Resource
    private TagService tagService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LuceneService luceneService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private TimelineService timelineService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            ArticleForm querySticky = new ArticleForm();
            querySticky.setIsSticky(true);
            querySticky.setStatus(ArticleStatusEnum.NORMAL.getIndex());
            querySticky.setOrderBy("article.priority DESC, article.create_time DESC");
            List<ArticleVo> stickyArticles = feBlogService.getArticleList(querySticky);
            model.addAttribute("stickies", stickyArticles);

            ArticleForm queryLatest = new ArticleForm();
            queryLatest.setCurrentPage(1);
            queryLatest.setLimit(6);
            queryLatest.setIsSticky(false);
            queryLatest.setStatus(ArticleStatusEnum.NORMAL.getIndex());
            queryLatest.setOrderBy("article.priority DESC, article.create_time DESC");
            model.addAttribute("lasts", feBlogService.getArticleList(queryLatest));
            model.addAttribute("lastsCount", feBlogService.countArticleList(queryLatest).getTotalCount());
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        }
        return "index";
    }

    @GetMapping(value = {"/category/{category}","/category/{category}/{currentPage}"})
    public String category(@PathVariable String category,@PathVariable(required = false) Integer currentPage, Model model) {
        try {
            Category query = new Category();
            query.setHref(category);
            query = categoryService.getOneCategory(query);

            PageTypeEnum page = PageTypeEnum.getPageTypeEnum(query.getPageType());

            ArticleForm articleForm = new ArticleForm();
            articleForm.setCategory(query.getName());
            List<ArticleVo> articles = getArticles(model, articleForm, currentPage, page.getLimit());
            model.addAttribute("name", query.getName());
            model.addAttribute("href", "/category/" + category + "/");

            getHotArticles(model, articles);

            return page.getName();
        } catch (NotFoundException ntf) {
            return "error/404";
        } finally {
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
            getPagePlugins(model);
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
            List<ArticleVo> articles = getArticles(model, articleForm, currentPage, PageTypeEnum.LIST.getLimit());
            model.addAttribute("name", tag);
            model.addAttribute("href", "/tag/" + tag + "/");

            getHotArticles(model, articles);
            return PageTypeEnum.LIST.getName();
        } catch (NotFoundException ntf) {
            return "error/404";
        } finally {
            getPagePlugins(model);
        }
    }

    @GetMapping(value = {"/archive","/archive/{currentPage}"})
    public String archive(Model model, @PathVariable(required = false) Integer currentPage) {
        try {
            List<ArticleVo> articles = getArticles(model, new ArticleForm(), currentPage, PageTypeEnum.LIST.getLimit());
            getHotArticles(model, articles);
            model.addAttribute("name", "归档");
            model.addAttribute("href", "/archive/");
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
        }
        return PageTypeEnum.LIST.getName();
    }

    @GetMapping(value = {"/blackboard"})
    public String blackboard(Model model) {
        try {
            model.addAttribute("comments", feBlogService.getComments(0L));
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        } catch (Exception e) {
            log.error("系统错误,", e);
        } finally {
            getPagePlugins(model);
        }
        return "blackboard";
    }

    @GetMapping(value = {"/coucou","/coucou/{currentPage}"})
    public String coucou(Model model, @PathVariable(required = false) Integer currentPage) {
        try {
            ArticleForm articleForm = new ArticleForm();
            articleForm.setCategory("凑凑");
            getArticles(model, articleForm, currentPage, 8);
            model.addAttribute("href", "/coucou/");
            model.addAttribute("latestComments", feBlogService.getLatestComments(articleForm));
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-coucou";
    }

    @GetMapping("/aboutme")
    public String aboutMe(Model model) {
        try {
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-about";
    }

    @GetMapping("/posts/{alias}")
    public String posts(@PathVariable String alias, Model model, HttpServletRequest request) {
        try {
            ArticleVo article = feBlogService.getOneArticle(alias);
            model.addAttribute("article", article);
            UserInfoVO userInfoVO = LoginUserUtil.getSessionUserWithoutThrow();
            feBlogService.addViews(article.getId(), userInfoVO.getId() == null ? request.getSession().getId() : String.valueOf(userInfoVO.getId()));

            model.addAttribute("comments", feBlogService.getComments(article.getId()));
            model.addAttribute("recommends", feBlogService.getRecommendArticle(article.getId()));

            Integer type = article.getType();
            return ArticleTypeEnum.getMsg(type);
        } catch (NotFoundException ntf) {
            return "error/404";
        } finally {
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
            getPagePlugins(model);
        }
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(name = "search", required = false) String search,
                         @RequestParam(name = "currentPage", required = false) Integer currentPage) {
        if (StringUtils.isNotEmpty(search)) {
            ArticleForm articleForm = new ArticleForm();
            articleForm.setContent(search);
            articleForm.setCurrentPage(currentPage == null ? 1 : currentPage);
            articleForm.setLimit(10);
            ResultListData result = luceneService.search(articleForm);
            model.addAttribute("list", result);
            model.addAttribute("search", search);
            model.addAttribute("pageInfo", new Page(result.getTotalCount(), result.getCurrentPage(), 10));
            model.addAttribute("href", "/search?search=" + search + "&currentPage=");
            getHotArticles(model, (List<ArticleVo>) result.getList());
        }
        getPagePlugins(model);
        return "page-search";
    }

    @GetMapping("/login")
    public RedirectView login(RedirectAttributes redirectAttributes,
                              @RequestParam(name = "ticket", required = false)String ticket,
                              @RequestParam(name = "redirect", required = false)String redirect) {
        if (StringUtils.isNotEmpty(ticket)) {
            LoginUserUtil.login(ticket);
        }
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUserWithoutThrow();
        if (userInfoVO.getId() == null) {
            String url = CommonConstant.BLOG_LOGIN_URL;
            redirectAttributes.addAttribute("redirect", redirect);
            redirectAttributes.addAttribute("login", url);
            return new RedirectView(CommonConstant.SSO_URL);
        }
        if (StringUtils.isNotEmpty(redirect)) {
            return new RedirectView(redirect);
        }
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public Object logout() {
        LoginUserUtil.logout();
        String ssoLogout = CommonConstant.SSO_URL + "/logout?redirect=https://edchu.cn";
        return new ModelAndView("redirect:" + ssoLogout);
    }

    @GetMapping("/friends")
    public String friends(Model model) {
        try {
            Friends query = new Friends();
            query.setStatus(FriendsLinkStatusEnum.APPROVE.getIndex());
            query.setOrderBy("modify_time DESC");
            model.addAttribute("friends", feBlogService.getFriendsList(query));
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-friends";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        try {
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-contact";
    }

    @GetMapping("/events")
    public String events(Model model) {
        try {
            model.addAttribute("categories", categoryService.searchCategory(new CategoryForm()));
        } catch (Exception e) {
            log.error("系统异常,", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-events";
    }

    @GetMapping("/subscribe")
    public String subscribe(Model model, @RequestParam String eid) {
        try {
            subscribeService.enableSubscribe(eid);
        } catch (BusinessException be) {
            model.addAttribute("message", be.getTextMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-subscribe";
    }

    @GetMapping("/unsubscribe")
    public String unsubscribe(Model model, @RequestParam String eid) {
        try {
            String mail = subscribeService.disableSubscribe(eid);
            model.addAttribute("subscribeList", subscribeService.getSubscribeList(mail));
        } catch (BusinessException be) {
            model.addAttribute("message", be.getTextMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
        } finally {
            getPagePlugins(model);
        }
        return "page-unsubscribe";
    }

    @RequestMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("hotTags", tagService.hotTags(10));
        return "/error/404";
    }

    private List<ArticleVo> getArticles(Model model, ArticleForm articleForm, Integer currentPage, Integer limit) {
        articleForm.setCurrentPage(currentPage == null ? 1 : currentPage);
        articleForm.setLimit(limit);
        articleForm.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        articleForm.setOrderBy("article.priority DESC, article.create_time DESC");
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

    private void getPagePlugins(Model model) {
        model.addAttribute("hotTags", tagService.hotTags(null));
        model.addAttribute("ig", feBlogService.getIgImages());
    }
}
