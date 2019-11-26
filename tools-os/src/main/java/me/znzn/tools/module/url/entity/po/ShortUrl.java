package me.znzn.tools.module.url.entity.po;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.component.BaseModel;

import java.io.Serializable;

/**
 * 短网址(ShortUrl)表实体类
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/19 13:29:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortUrl extends BaseModel implements Serializable{
    private static final long serialVersionUID = 440357301361849812L;

    /**
     * 原url
     */
    private String originUrl;

    /**
     * 状态
     */
    private Integer status;


}
