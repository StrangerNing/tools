package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.ArticleComment;
import java.util.List;

import me.znzn.tools.module.blog.entity.vo.ArticleCommentVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/03/07 23:43:35
 * @copyright
 */
@Mapper
public interface ArticleCommentMapper extends BaseDao<ArticleComment> {

    /**
     * 根据文章id获取评论列表
     * @param articleId
     * @return
     */
    List<ArticleCommentVo> selectVoByArticleId(Long articleId);

    /**
     * 获取最新的评论
     * @param articleForm
     * @return
     */
    List<ArticleCommentVo> selectLatestComments(ArticleForm articleForm);
}
