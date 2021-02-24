package me.znzn.tools.module.blog.entity.po;

import lombok.Data;
import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
@Data
public class Markdown implements Serializable {
    private static final long serialVersionUID = -58322545125010778L;
    /**
     * 文章id
     */
    private Long id;

    /**
     * markdown源码
     */
    private String markdown;


}
