package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.po.Category;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/28 21:33:12
 * @copyright
 */
@Mapper
public interface CategoryMapper extends BaseDao<Category> {

    /**
     * 根据文章id获取分类
     * @param id
     * @return
     */
    List<Category> selectCategoryByArticleId(Long id);

}
