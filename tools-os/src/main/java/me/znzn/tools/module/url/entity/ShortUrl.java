package me.znzn.tools.module.url.entity;

import java.util.Date;
import lombok.Data;
import java.io.Serializable;

/**
 * 短网址(ShortUrl)表实体类
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/11 15:47:34
 */
@Data
public class ShortUrl implements Serializable{
    private static final long serialVersionUID = 362145978496390843L;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 原url
     */
    private String originUrl;

    /**
     * 短url
     */
    private String shortUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createAccount;


}
