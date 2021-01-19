package me.znzn.tools.module.user.entity.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Data
public class RegisterForm {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "请输入密码")
    private String password;

    @NotBlank(message = "请输入确认密码")
    private String confirmPassword;

    private String captchaCode;

}
