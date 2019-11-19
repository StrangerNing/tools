package me.znzn.tools.module.user.entity;

import java.util.Date;
import lombok.Data;
import java.io.Serializable;

/**
 * 用户表(User)表实体类
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/19 13:09:47
 */
@Data
public class User implements Serializable{
    private static final long serialVersionUID = 460885777953346968L;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别 0男1女2未知
     */
    private Integer sex;

    /**
     * 状态0冻结1有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createEmp;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private Long modifyEmp;

    /**
     * 备注
     */
    private String remark;


}
