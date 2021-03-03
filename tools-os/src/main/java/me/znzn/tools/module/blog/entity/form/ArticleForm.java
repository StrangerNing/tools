package me.znzn.tools.module.blog.entity.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.module.blog.entity.po.Article;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleForm extends Article {

    /**
     * 标签
     */
    public String tag;

    /**
     * 是否置顶
     */
    public Boolean isSticky;

    /**
     * 分类
     */
    public String category;
}
