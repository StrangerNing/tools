package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.module.blog.entity.po.Subscribe;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/08/05 13:57:52
 * @copyright
 */
@Mapper
public interface SubscribeMapper {

    /**
     * 根据对象属性插入
     *
     * @param subscribe 实例对象
     * @return {@link Integer}
     */
    Integer insertByProperty(Subscribe subscribe);

    /**
     * 根据对象列表属性插入
     *
     * @param list 实例列表
     * @return {@link Integer}
     */
    Integer insertBatchByProperty(List<Subscribe> list);

    /**
     * 根据对象属性带主键更新
     *
     * @param subscribe 实例对象
     * @return {@link Integer}
     */
    Integer updateByPrimaryKey(Subscribe subscribe);

    /**
    * 根据对象属性带主键列表批量更新
    *
    * @param list 实例对象列表
    * @return {@link Integer}
    */
    Integer updateBatchByPrimaryKey(List<Subscribe> list);

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
     * @param subscribe 实例对象
     * @return {@link Integer}
     */
    Integer deleteByProperty(Subscribe subscribe);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return {@link Subscribe}
     */
    Subscribe selectByPrimaryKey(Long id);

    /**
     * 根据属性进行查询
     *
     * @param subscribe 实例对象
     * @return {@link List<Subscribe>}
     */
    List<Subscribe> selectByProperty(Subscribe subscribe);

    /**
     * 根据属性进行查询统计
     *
     * @param subscribe 实例对象
     * @return {@link Long}
     */
    Long countByProperty(Subscribe subscribe);
}
