package me.znzn.tools.module.oss.entity.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileForm extends BaseModel {

    private Long id;

    private String name;

    private Integer type;

    private String style;
}
