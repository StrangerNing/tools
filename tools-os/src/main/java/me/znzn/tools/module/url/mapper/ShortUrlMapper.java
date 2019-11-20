package me.znzn.tools.module.url.mapper;

import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Mapper
public interface ShortUrlMapper extends BaseDao<ShortUrl> {

}
