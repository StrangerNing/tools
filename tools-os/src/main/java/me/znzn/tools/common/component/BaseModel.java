package me.znzn.tools.common.component;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long modifyAccount;

    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    private String remark;

    public Integer getStartIndex() {
        if (getLimit() != null || getCurrentPage() != null) {
            return getLimit() * (getCurrentPage() - 1);
        } else {
            return null;
        }
    }

}
