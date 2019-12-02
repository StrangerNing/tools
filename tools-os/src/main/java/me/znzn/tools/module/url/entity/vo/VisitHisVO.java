package me.znzn.tools.module.url.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhuzening
 * @date 2019/12/2
 * @since 1.0
 */
@Data
public class VisitHisVO {

    private BigDecimal lng;

    private BigDecimal lat;

    private Integer count;
}
