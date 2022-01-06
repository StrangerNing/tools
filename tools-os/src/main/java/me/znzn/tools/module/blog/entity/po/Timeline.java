package me.znzn.tools.module.blog.entity.po;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2022/01/02 01:36:27
 * @copyright
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Timeline extends BaseModel implements Serializable {
    private static final long serialVersionUID = -71718682476094773L;

    private Long id;

    private Date publishTime;

    private String title;

    private String description;

    private Integer type;

    private Integer status;

}
