package me.znzn.tools.module.oss.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileReturnVo extends BaseModel {

    private Long id;

    private String name;

    private Integer type;

    /**
     * url
     */
    private String url;
}
