package me.znzn.tools.module.oss.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;
import me.znzn.tools.common.constant.CommonConstant;

import java.io.Serializable;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileReturnVo extends BaseModel implements Serializable {

    private static final long serialVersionUID = 8066280598625648896L;

    private Long id;

    private String name;

    private Integer type;

    /**
     * url
     */
    private String url;

    private String prefix = CommonConstant.FILE_REQUEST_PREFIX;
}
