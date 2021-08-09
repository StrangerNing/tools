package me.znzn.tools.module.blog.entity.po;

import java.util.Date;
import lombok.Data;
import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/08/05 13:57:52
 * @copyright
 */
@Data
public class Unsubscribe implements Serializable {
    private static final long serialVersionUID = -81674098288496242L;

    private Long id;

    private String mail;

    private Integer type;

    private Date createTime;

    private String remark;

}
