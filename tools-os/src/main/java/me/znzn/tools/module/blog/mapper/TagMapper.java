package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.po.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
@Mapper
public interface TagMapper extends BaseDao<Tag> {

    /**
     * 插入记录避免重复
     * @param tag
     * @return
     */
    Long insertAvoidDuplicate(Tag tag);

    /**
     * 搜索tag
     * @param tag
     * @return
     */
    List<Tag> searchTag(String tag);

    /**
     * 获取热门标签
     * @param size
     * @return
     */
    List<Tag> hotTags(Integer size);

    /**
     * 搜索文章所有标签
     * @param id
     * @return
     */
    List<Tag> selectArticleTagsByArticleId(Long id);

}
