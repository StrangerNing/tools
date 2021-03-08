package me.znzn.tools.module.blog.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.module.blog.entity.po.ArticleComment;

import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCommentVo extends ArticleComment {

    List<ArticleCommentVo> childrenComments;
}
