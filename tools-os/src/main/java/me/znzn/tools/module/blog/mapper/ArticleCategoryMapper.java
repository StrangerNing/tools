package me.znzn.tools.module.blog.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.po.ArticleCategory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/28 21:33:36
 */
@Mapper
public interface ArticleCategoryMapper extends BaseDao<ArticleCategory> {


}
