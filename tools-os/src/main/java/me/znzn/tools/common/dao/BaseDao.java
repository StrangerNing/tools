package me.znzn.tools.common.dao;

import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
public interface BaseDao<T> {
    /**
     * 根据对象属性插入
     *
     * @param map 实例对象
     * @return java.lang.Long
     */
    Long insertByProperty(T map);

    /**
     * 根据对象列表属性插入
     *
     * @param list 实例列表
     * @return java.lang.Integer
     */
    Integer insertBatchByProperty(List<T> list);

    /**
     * 根据对象属性带主键更新
     *
     * @param map 实例对象
     * @return java.lang.Integer
     */
    Integer updateByPrimaryKey(T map);

    /**
     * 根据对象属性带主键列表批量更新
     *
     * @param list 实例对象列表
     * @return java.lang.Integer
     */
    Integer updateBatchByPrimaryKey(List<T> list);

    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return java.lang.Integer
     */
    Integer deleteByPrimaryKey(Long id);

    /**
     * 根据主键列表批量删除
     *
     * @param list 主键列表
     * @return java.lang.Integer
     */
    Integer deleteBatchByPrimaryKey(List<Long> list);

    /**
     * 根据属性删除
     *
     * @param map 实例对象
     * @return java.lang.Integer
     */
    Integer deleteByProperty(T map);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return me.znzn.tools.module.user.entity.po.User
     */
    T selectByPrimaryKey(Long id);

    /**
     * 根据属性进行查询
     *
     * @param map 实例对象
     * @return java.util.List<me.znzn.tools.module.user.entity.po.User>
     */
    List<T> selectByProperty(T map);

    /**
     * 根据属性进行查询统计
     *
     * @param map 实例对象
     * @return java.lang.Long
     */
    Long countByProperty(T map);
}
