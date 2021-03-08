package me.znzn.tools.module.blog.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/03/07 23:43:35
 * @copyright
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleComment extends BaseModel implements Serializable {
    private static final long serialVersionUID = -87165600449145581L;

        /**
     * 文章id
     */
    private Long articleId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 内容
     */
    private String content;

    /**
     * 回复的评论id
     */
    private Long replyId;

    /**
     * 发布人
     */
    private String createName;

    /**
     * 发布人邮箱
     */
    private String createEmail;

    /**
     * 发布人网站
     */
    private String createWebsite;

}
