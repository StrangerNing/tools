package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.po.Markdown;
import java.util.List;

/**
 * 服务接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
public interface MarkdownService {

    /**
     * 根据对象属性插入
     *
     * @param markdown 实例对象
     * @return {@link Integer}
     */
    Long insertByProperty(Markdown markdown);

    /**
     * 根据对象属性列表批量插入
     *
     * @param list 实例列表
     * @return {@link Integer}
     */
    Integer insertBatchByProperty(List<Markdown> list);

    /**
     * 根据对象属性带主键更新
     *
     * @param markdown 实例对象
     * @return {@link Integer}
     */
    Integer updateByPrimaryKey(Markdown markdown);

    /**
     * 根据对象属性带主键列表批量更新
     *
     * @param list 实例对象列表
     * @return {@link Integer}
     */
    Integer updateBatchByPrimaryKey(List<Markdown> list);

    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return {@link Integer}
     */
    Integer deleteByPrimaryKey(Long id);

    /**
     * 根据主键列表批量删除
     *
     * @param list 主键列表
     * @return {@link Integer}
     */
    Integer deleteBatchByPrimaryKey(List<Long> list);

    /**
     * 根据属性删除
     *
     * @param markdown 实例对象
     * @return {@link Integer}
     */
    Integer deleteByProperty(Markdown markdown);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return {@link Markdown}
     */
    Markdown selectByPrimaryKey(Long id);

    /**
     * 根据属性进行查询
     *
     * @param markdown 实例对象
     * @return {@link List<Markdown>}
     */
    List<Markdown> selectByProperty(Markdown markdown);

    /**
     * 根据属性进行查询统计
     *
     * @param markdown 实例对象
     * @return {@link Integer}
     */
    Integer countByProperty(Markdown markdown);

}
