package me.znzn.tools.module.user.mapper;
import me.znzn.tools.module.user.entity.po.ApiKey;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ApiKey)表数据库访问层
 *
 * @author
 * @version 1.0.0
 * @date 2020/04/21 13:53:25
 * @copyright
 */
@Mapper
public interface ApiKeyMapper{

    /**
     * 根据对象属性插入
     *
     * @param apiKey 实例对象
     * @return java.lang.Long
     */
    Long insertByProperty(ApiKey apiKey);

    /**
     * 根据对象列表属性插入
     *
     * @param list 实例列表
     * @return java.lang.Integer
     */
    Integer insertBatchByProperty(List<ApiKey> list);

    /**
     * 根据对象属性带主键更新
     *
     * @param apiKey 实例对象
     * @return java.lang.Integer
     */
    Integer updateByPrimaryKey(ApiKey apiKey);

    /**
    * 根据对象属性带主键列表批量更新
    *
    * @param list 实例对象列表
    * @return java.lang.Integer
    */
    Integer updateBatchByPrimaryKey(List<ApiKey> list);

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
     * @param apiKey 实例对象
     * @return java.lang.Integer
     */
    Integer deleteByProperty(ApiKey apiKey);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return me.znzn.tools.module.user.entity.po.ApiKey
     */
    ApiKey selectByPrimaryKey(Long id);

    /**
     * 根据属性进行查询
     *
     * @param apiKey 实例对象
     * @return java.util.List<me.znzn.tools.module.user.entity.po.ApiKey>
     */
    List<ApiKey> selectByProperty(ApiKey apiKey);

    /**
     * 根据属性进行查询统计
     *
     * @param apiKey 实例对象
     * @return java.lang.Long
     */
    Long countByProperty(ApiKey apiKey);
}
