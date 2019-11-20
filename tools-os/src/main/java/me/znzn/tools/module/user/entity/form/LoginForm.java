package me.znzn.tools.module.user.entity.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Data
public class LoginForm {

    @NotBlank(message = "用户名不可为空")
    private String username;

    @NotBlank(message = "密码不可为空")
    private String password;
}
