package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.Article;
import java.util.List;

import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
@Mapper
public interface ArticleMapper extends BaseDao<Article> {

    /**
     * 根据条件查询文章集合
     * @param articleForm
     * @return
     */
    List<ArticleVo> selectArticleList(ArticleForm articleForm);

    /**
     * 获取符合条件的文章条数
     * @param articleForm
     * @return
     */
    Integer countArticle(ArticleForm articleForm);

    /**
     * 获取文章详情
     * @param id
     * @return
     */
    ArticleVo selectArticleById(Long id);

    /**获取文章详情
     * @param alias
     * @return
     */
    ArticleVo selectArticleByAlias(String alias);

    /**
     * 根据标签集合获取文章集合
     * @param articleVo
     * @return
     */
    List<ArticleVo> selectArticleByTags(ArticleVo articleVo);

    /**
     * 更新文章阅读量
     * @param articleList
     */
    void updateArticleViews(List<Article> articleList);

}
