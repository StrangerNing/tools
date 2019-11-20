package me.znzn.tools.module.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Data
public class UserInfoVO {

    private String token;

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
    @JsonIgnore
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
