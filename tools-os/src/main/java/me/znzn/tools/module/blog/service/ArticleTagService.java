package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.po.ArticleTag;
import java.util.List;

/**
 * 服务接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:23:02
 * @copyright
 */
public interface ArticleTagService {

    /**
     * 根据对象属性插入
     *
     * @param articleTag 实例对象
     * @return {@link Integer}
     */
    Long insertByProperty(ArticleTag articleTag);

    /**
     * 根据对象属性列表批量插入
     *
     * @param list 实例列表
     * @return {@link Integer}
     */
    Integer insertBatchByProperty(List<ArticleTag> list);

    /**
     * 根据对象属性带主键更新
     *
     * @param articleTag 实例对象
     * @return {@link Integer}
     */
    Integer updateByPrimaryKey(ArticleTag articleTag);

    /**
     * 根据对象属性带主键列表批量更新
     *
     * @param list 实例对象列表
     * @return {@link Integer}
     */
    Integer updateBatchByPrimaryKey(List<ArticleTag> list);

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
     * @param articleTag 实例对象
     * @return {@link Integer}
     */
    Integer deleteByProperty(ArticleTag articleTag);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return {@link ArticleTag}
     */
    ArticleTag selectByPrimaryKey(Long id);

    /**
     * 根据属性进行查询
     *
     * @param articleTag 实例对象
     * @return {@link List<ArticleTag>}
     */
    List<ArticleTag> selectByProperty(ArticleTag articleTag);

    /**
     * 根据属性进行查询统计
     *
     * @param articleTag 实例对象
     * @return {@link Integer}
     */
    Integer countByProperty(ArticleTag articleTag);

}
