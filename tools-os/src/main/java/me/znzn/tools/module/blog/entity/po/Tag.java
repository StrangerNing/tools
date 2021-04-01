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
public class Tag implements Serializable {
    private static final long serialVersionUID = 4603943882286581184L;

    private Long id;

    private String tag;

}
