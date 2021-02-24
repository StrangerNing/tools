package me.znzn.tools.module.blog.entity.po;

import java.util.Date;
import lombok.Data;
import me.znzn.tools.common.component.BaseModel;

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
public class Article extends BaseModel implements Serializable {
    private static final long serialVersionUID = 858479821984183709L;

        /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 别名
     */
    private String alias;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 权限
     */
    private Integer permission;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 作者
     */
    private String author;

    /**
     * 允许评论
     */
    private Integer comment;

    /**
     * 阅读数
     */
    private Integer views;

    /**
     * 剩余阅读时间
     */
    private Integer minutes;

    /**
     * 预定发布时间
     */
    private Date orderPublishTime;

    /**
     * 编辑类型
     */
    private Integer editType;

    private Integer version;

    private String thumb;

}
