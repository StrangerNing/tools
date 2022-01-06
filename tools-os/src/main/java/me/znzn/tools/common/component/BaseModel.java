package me.znzn.tools.common.component;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/30
 */

@Getter
@Setter
public class BaseModel {

    private Long id;

    private Integer currentPage;

    private Integer limit;

    private Integer startIndex;

    private Long createAccount;

    private Date createTime;

    private Long modifyAccount;

    private Date modifyTime;

    private String remark;

    private String orderBy = "create_time DESC";

    private Integer year;

    private Integer minYear;

    private Integer maxYear;

    public Integer getStartIndex() {
        if (getLimit() != null || getCurrentPage() != null) {
            return getLimit() * (getCurrentPage() - 1);
        } else {
            return null;
        }
    }

}
