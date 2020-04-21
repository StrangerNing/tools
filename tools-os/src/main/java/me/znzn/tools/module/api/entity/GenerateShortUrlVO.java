package me.znzn.tools.module.api.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/4/21
 */
@Data
public class GenerateShortUrlVO {

    @NotBlank(message = "ak不可为空")
    String ak;

    @NotBlank(message = "url不可为空")
    String url;
}
