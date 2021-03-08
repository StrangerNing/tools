package me.znzn.tools.common.component;

import cn.hutool.core.collection.CollectionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回结果包装类
 *
 * @author 吴佰川
 * @version 1.0
 * @date 2019/3/1 10:49
 */
public class Page implements Serializable {
    private static final long serialVersionUID = -6910120640655966888L;

    private Integer currentPage;

    private Integer limit;

    private Integer totalCount;

    private List<Integer> pageNumList;

    private Integer totalPage;

    private Boolean isLastPage;

    private Boolean isFirstPage;

    public void setPageNumList() {
        Integer totalPage = new Integer(this.totalPage);
        if (totalPage < 6) {
            List<Integer> pageNumList = new ArrayList<>();
            for (int i = 1; i <= totalPage; i++) {
                pageNumList.add(i);
            }
            this.pageNumList =  pageNumList;
            return;
        }
        if (currentPage < 4) {
            this.pageNumList = CollectionUtil.newArrayList(1,2,3,4,5);
            return;
        }
        if (currentPage + 2 > this.totalPage) {
            this.pageNumList = CollectionUtil.newArrayList(totalPage - 4, totalPage - 3, totalPage - 2, totalPage - 1, totalPage);
            return;
        }
        this.pageNumList = CollectionUtil.newArrayList(currentPage - 2, currentPage - 1, currentPage, currentPage + 1, currentPage + 2);
    }

    public void setTotalPage() {
        if (totalCount == null || limit == null || limit == 0) {
            this.totalPage = 0;
            return;
        }
        this.totalPage = (totalCount + limit - 1) / limit;
    }

    public Page(int totalCount, int currentPage, int limit) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.limit = limit;
        setTotalPage();
        setPageNumList();
        this.isFirstPage = currentPage == 1;
        this.isLastPage = totalPage.equals(currentPage);
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<Integer> getPageNumList() {
        return pageNumList;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Boolean isFirstPage() {
        return isFirstPage;
    }

    public Boolean isLastPage() {
        return isLastPage;
    }
}
