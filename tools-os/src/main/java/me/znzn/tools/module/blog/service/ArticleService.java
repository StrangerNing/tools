package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

import java.util.List;

/**
 * 服务接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
public interface ArticleService {

    /**
     * 新增文章
     * @param articleVo 文章
     * @param loginUser 登录用户
     * @return Long
     */
    Long addArticle(ArticleVo articleVo, UserInfoVO loginUser);

    /**
     * 编辑文章
     * @param articleVo
     * @param loginUser
     * @return
     */
    Boolean editArticle(ArticleVo articleVo, UserInfoVO loginUser);

    /**
     * 获取文章列表
     * @param articleForm 文章
     * @param loginUser 登录用户
     * @return List
     */
    List<ArticleVo> articleList(ArticleForm articleForm, UserInfoVO loginUser);

    /**
     * 获取文章条数
     * @param articleForm
     * @param loginUser
     * @return
     */
    Integer countArticle(ArticleForm articleForm, UserInfoVO loginUser);

    /**
     * 获取文章详情
     * @param id
     * @return
     */
    ArticleVo articleDetail(Long id);

    /**
     * 删除文章
     * @param id
     */
    void deleteArticle(Long id);

    /**
     * 刷新索引
     */
    void refreshIndex();
}
