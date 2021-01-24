package me.znzn.tools.common.component;

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
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -6910120640655966888L;
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 记录总数 参数类型:输出
     */
    private Long total;

    /**
     * 记录结果集
     */
    private List<T> list;

    public Page() {

    }

    /**
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param total    总记录数
     */
    public Page(int pageNum, int pageSize, Long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    /**
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param total    总记录数
     * @param list     数据List
     */
    public Page(int pageNum, int pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    /**
     * @param list  数据List
     * @param total 总记录数
     */
    public Page(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> Page<T> empty() {
        return new Page<T>(new ArrayList<T>(), 0L);
    }

    public static <T> Page<T> with(Long total, List<T> list) {
        return new Page<T>(list, total);
    }

    public static <T> Page<T> with(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        return new Page<T>(pageNum, pageSize, total, list);
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" + "pageNum=" + pageNum + ", pageSize=" + pageSize + ", total=" + total + ", list=" + list
                + '}';
    }
}
