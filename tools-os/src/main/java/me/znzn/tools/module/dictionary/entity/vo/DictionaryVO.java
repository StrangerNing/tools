package me.znzn.tools.module.dictionary.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

/**
 * @author zhuzening
 * @date 2019/11/27
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryVO extends BaseModel {

    /**
     * 变量名称
     */
    private String name;

    /**
     * 变量值
     */
    private String value;

    /**
     * 状态
     */
    private Integer status;

    private String createName;

    private String modifyName;

    private Integer version;
}
