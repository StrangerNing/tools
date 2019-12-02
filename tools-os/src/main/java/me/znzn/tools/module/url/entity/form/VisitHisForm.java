package me.znzn.tools.module.url.entity.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhuzening
 * @date 2019/12/2
 * @since 1.0
 */

@Data
public class VisitHisForm {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 短链接id
     */
    private Long urlId;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 地址
     */
    private String address;

    /**
     * 访问时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
}
