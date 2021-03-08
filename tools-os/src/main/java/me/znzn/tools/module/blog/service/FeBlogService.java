package me.znzn.tools.module.blog.service;

import me.znzn.tools.common.component.Page;
import me.znzn.tools.common.component.ResultListData;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.ArticleComment;
import me.znzn.tools.module.blog.entity.vo.ArticleCommentVo;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/28
 */
public interface FeBlogService {

    /**
     * 获取文章集合
     * @param articleForm
     * @return
     */
    List<ArticleVo> getArticleList(ArticleForm articleForm);

    /**
     * 获取推荐文章
     * @param id
     * @return
     */
    List<ArticleVo> getRecommendArticle(Long id);

    /**
     * 计数
     * @param articleForm
     * @return
     */
    Page countArticleList(ArticleForm articleForm);

    /**
     * 获取一篇文章
     * @param alias
     * @return
     */
    ArticleVo getOneArticle(String alias);

    /**
     * 获取文章评论
     * @param articleId
     * @return
     */
    ResultListData getComments(Long articleId);

    /**
     * 添加文章评论
     * @param articleComment
     * @param loginUser
     */
    void addArticleComment(ArticleComment articleComment, UserInfoVO loginUser);

    /**
     * 阅读量
     * @param id
     */
    void addViews(Long id);
}
