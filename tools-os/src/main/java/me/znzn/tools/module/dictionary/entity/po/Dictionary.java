package me.znzn.tools.module.dictionary.entity.po;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

import java.io.Serializable;

/**
 * 数据字典(Dictionary)表实体类
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/27 11:08:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dictionary extends BaseModel implements Serializable{
    private static final long serialVersionUID = -23088478231674441L;

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

    private Integer version;

}
