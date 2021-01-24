package me.znzn.tools.common.component;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回前端集合对象类
 */
@Data
public class ResultListData<T> implements Serializable {

    /** 具体值 .*/
    private T list;

    /** 当前页 **/
    private Integer currentPage;

    /** 总条数 **/
    private Integer totalCount;
}
