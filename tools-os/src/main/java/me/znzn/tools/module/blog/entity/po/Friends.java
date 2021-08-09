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
 * @date 2021/06/24 19:10:48
 * @copyright
 */
@Data
public class Friends extends BaseModel implements Serializable {
    private static final long serialVersionUID = 778453174333745638L;

    private Long id;

    private String nickname;

    private String email;

    private String website;

    private String name;

    private String icon;

    private String introduction;

    private String message;

    private Integer status;

    private Integer version;

}
