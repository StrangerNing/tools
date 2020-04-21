package me.znzn.tools.module.user.entity.form;

import lombok.Data;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/4/20
 */
@Data
public class ApiKeyForm {

    private Long id;
    /**
     * 应用名称
     */
    private String name;

    /**
     * 模块
     */
    private Long module;

    /**
     * 限制类型 0白名单1黑名单
     */
    private Integer limitType;

    /**
     * 限制允许ip
     */
    private String limitIp;


    private String remark;

    private Long createId;

    private Integer version;
}
