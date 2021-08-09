package me.znzn.tools.module.blog.entity.po;

import java.util.Date;
import lombok.Data;
import java.io.Serializable;

/**
 * 实体
 *
 * @author
 * @version 1.0.0
 * @date 2021/08/03 14:14:08
 * @copyright
 */
@Data
public class Eid implements Serializable {
    private static final long serialVersionUID = -48467380927582976L;

    private Long id;

    private String eid;

    private String email;

    private Integer type;

    private Integer status;

    private String template;

    private String nickname;

    private Date createTime;

    private String remark;

}
