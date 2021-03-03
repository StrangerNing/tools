package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;

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
     * 计数
     * @param articleForm
     * @return
     */
    Integer countArticleList(ArticleForm articleForm);
}
