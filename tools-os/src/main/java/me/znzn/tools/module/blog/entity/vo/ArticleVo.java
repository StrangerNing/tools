package me.znzn.tools.module.blog.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.module.blog.entity.po.Article;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.entity.po.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleVo extends Article implements Serializable {

    private static final long serialVersionUID = -8397539432385057251L;

    private List<Tag> tags;

    private List<Category> categories;

    private String markdown;

    private String thumbPreview;
}
