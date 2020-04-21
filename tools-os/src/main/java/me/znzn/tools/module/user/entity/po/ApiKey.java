package me.znzn.tools.module.user.entity.po;

import java.util.Date;
import lombok.Data;
import java.io.Serializable;

/**
 * (ApiKey)表实体类
 *
 * @author
 * @version 1.0.0
 * @date 2020/04/21 13:53:25
 * @copyright
 */
@Data
public class ApiKey implements Serializable{
    private static final long serialVersionUID = -24360348635024174L;

    private Long id;
        /**
     * 应用名称
     */
    private String name;

    /**
     * 访问应用
     */
    private String ak;

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

    /**
     * 用户id
     */
    private Long createId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改id
     */
    private Long modifyId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    private Integer version;


}
