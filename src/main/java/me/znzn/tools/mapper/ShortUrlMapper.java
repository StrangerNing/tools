package me.znzn.tools.mapper;
import java.util.List;

import me.znzn.tools.entity.ShortUrl;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短网址(ShortUrl)表数据库访问层
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/11 15:47:34
 * @copyright www.ucarinc.com All Rights Reserved.
 */
@Mapper
public interface ShortUrlMapper{

    /**
     * 根据对象属性插入
     *
     * @param shortUrl 实例对象
     * @return java.lang.Long
     */
    Long insertByProperty(ShortUrl shortUrl);

    /**
     * 根据对象列表属性插入
     *
     * @param list 实例列表
     * @return java.lang.Integer
     */
    Integer insertBatchByProperty(List<ShortUrl> list);

    /**
     * 根据对象属性带主键更新
     *
     * @param shortUrl 实例对象
     * @return java.lang.Integer
     */
    Integer updateByPrimaryKey(ShortUrl shortUrl);

    /**
    * 根据对象属性带主键列表批量更新
    *
    * @param list 实例对象列表
    * @return java.lang.Integer
    */
    Integer updateBatchByPrimaryKey(List<ShortUrl> list);

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
     * @param shortUrl 实例对象
     * @return java.lang.Integer
     */
    Integer deleteByProperty(ShortUrl shortUrl);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return .me.znzn.tools.entity.ShortUrl
     */
    ShortUrl selectByPrimaryKey(Long id);

    /**
     * 根据属性进行查询
     *
     * @param shortUrl 实例对象
     * @return java.util.List<.me.znzn.tools.entity.ShortUrl>
     */
    List<ShortUrl> selectByProperty(ShortUrl shortUrl);

    /**
     * 根据属性进行查询统计
     *
     * @param shortUrl 实例对象
     * @return java.lang.Long
     */
    Long countByProperty(ShortUrl shortUrl);
}
