package me.znzn.tools.module.user.entity.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.BaseModel;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Slf4j
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 5443017913386436929L;

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

    private String roles;

    private String avatar;

    private String avatarUrl;

    private String nickname;

    private String googleId;

    public String getAvatarUrl() {
        if (this.avatar == null) {
            return null;
        }
        return CommonConstant.FILE_REQUEST_PREFIX + this.avatar;
    }

    public void setCreateUser(BaseModel model) {
        model.setCreateAccount(id);
        model.setCreateTime(new Date());
        model.setModifyAccount(id);
        model.setModifyTime(new Date());
    }

    public void setModifyUser(BaseModel modify) {
        modify.setModifyAccount(id);
        modify.setModifyTime(new Date());
    }

    public Boolean isAdmin() {
        if (roles == null) {
            return false;
        }
        try {
            JSONArray rolesArray = JSON.parseArray(roles);
            return rolesArray.contains("admin");
        } catch (Exception e) {
            log.error("JSON反序列化失败", e);
            return false;
        }
    }

    public Boolean hasOperateAuth(BaseModel baseModel) {
        if (isAdmin()) {
            return true;
        }
        return id.equals(baseModel.getCreateAccount());
    }

    public void hasOperateAuthThrowException(BaseModel baseModel) {
        if (!hasOperateAuth(baseModel)) {
            throw new BusinessException("没有操作权限");
        }
    }

    public void setOperateAccount(BaseModel baseModel) {
        if (isAdmin()) {
            return;
        }
        baseModel.setCreateAccount(id);
    }

}
