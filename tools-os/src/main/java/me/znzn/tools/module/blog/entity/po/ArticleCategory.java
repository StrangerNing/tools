package me.znzn.tools.module.blog.entity.po;

import lombok.Data;
import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/28 21:33:36
 * @copyright
 */
@Data
public class ArticleCategory implements Serializable {
    private static final long serialVersionUID = 738340445909890396L;

    private Long id;
        /**
     * 文章id
     */
    private Long articleId;

    /**
     * 分类id
     */
    private Long categoryId;


}
