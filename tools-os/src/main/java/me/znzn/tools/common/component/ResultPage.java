package me.znzn.tools.common.component;

import java.util.List;

/**
 * @author:liu.yucheng
 * @Data:2019-3-16 16:38
 * @version:1.0
 */
public class ResultPage {
    //总记录数
    private int totalCount;
    //每页分页条数
    private int pageSize;
    //总页数
    private int totalPage;
    //当前页数
    private int currentPage;
    //列表数据
    private List<?> list;

    /**
     * 分页
     * @param totalCount
     * @param pageSize
     * @param totalPage
     * @param currentPage
     * @param list
     */
    @Deprecated
    public ResultPage(int totalCount, int pageSize, int totalPage, int currentPage, List<?> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.list = list;
    }

    /**
     * 统一构造方法
     * @param totalCount
     * @param pageSize
     * @param currentPage
     * @param list
     */
    public ResultPage(int totalCount, int pageSize, int currentPage, List<?> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.list = list;
        this.totalPage = totalCount / pageSize * pageSize < totalCount ? totalCount / pageSize + 1 : totalCount / pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

}
