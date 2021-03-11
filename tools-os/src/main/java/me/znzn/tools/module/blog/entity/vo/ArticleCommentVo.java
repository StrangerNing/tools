package me.znzn.tools.module.blog.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.blog.entity.po.ArticleComment;
import org.apache.commons.lang3.StringUtils;

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

    String avatar;

    String articleHref;

    public String getAvatar() {
        if (StringUtils.isEmpty(this.avatar)) {
            return null;
        }
        return CommonConstant.FILE_REQUEST_PREFIX + this.avatar;
    }

    public String getArticleHref() {
        if (StringUtils.isEmpty(this.articleHref)) {
            return null;
        }
        return CommonConstant.ARTICLE_URL_PREFIX + articleHref;
    }
}
