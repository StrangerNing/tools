package me.znzn.tools.module.oss.entity.po;

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
 * @date 2021/01/27 11:23:13
 * @copyright
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class File extends BaseModel implements Serializable {
    private static final long serialVersionUID = -48906765681154955L;

    private Long id;

    private String name;

    private Integer type;

}
