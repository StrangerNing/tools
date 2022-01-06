package me.znzn.tools.module.url.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zhuzening
 * @date 2019/11/25
 * @since 1.0
 */
@Data
public class ShortUrlVO {

    private Long id;

    private String shortUrl;

    private String originUrl;

    private Date createTime;

    private Long createAccount;

    private String createName;

    private Integer status;
}
