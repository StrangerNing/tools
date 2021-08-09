package me.znzn.tools.module.blog.entity.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/08/05 13:57:52
 * @copyright
 */
@Data
public class Subscribe implements Serializable {
    private static final long serialVersionUID = 426963120321341023L;

    private Long id;

    private String mail;

    private Integer type;

    private Integer enable;

    private Date createTime;

    private String remark;

}
