package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.po.ArticleTag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:23:02
 * @copyright
 */
@Mapper
public interface ArticleTagMapper extends BaseDao<ArticleTag> {

}
