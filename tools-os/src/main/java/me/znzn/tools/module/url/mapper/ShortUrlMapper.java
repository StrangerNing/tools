package me.znzn.tools.module.url.mapper;

import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.url.entity.ShortUrl;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短网址(ShortUrl)表数据库访问层
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/11 15:47:34
 */
@Mapper
public interface ShortUrlMapper extends BaseDao<ShortUrl> {

}
