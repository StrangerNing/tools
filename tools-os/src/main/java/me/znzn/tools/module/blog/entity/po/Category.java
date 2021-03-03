package me.znzn.tools.module.blog.entity.po;

import lombok.Data;
import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/28 21:33:12
 * @copyright
 */
@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 213078052535726783L;

    private Long id;
        /**
     * 分类名
     */
    private String name;

    /**
     * 分类颜色
     */
    private String color;

    /**
     * 地址
     */
    private String href;

    /**
     * 排序
     */
    private Integer index;

}
