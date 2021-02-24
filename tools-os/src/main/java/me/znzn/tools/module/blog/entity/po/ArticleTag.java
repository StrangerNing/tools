package me.znzn.tools.module.blog.entity.po;

import lombok.Data;
import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:23:02
 * @copyright
 */
@Data
public class ArticleTag implements Serializable {
    private static final long serialVersionUID = 690792148657747085L;

    private Long id;

    private Long articleId;

    private Long tagId;

}
