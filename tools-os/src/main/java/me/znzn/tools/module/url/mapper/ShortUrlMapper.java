package me.znzn.tools.module.url.mapper;

import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.entity.vo.ShortUrlVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Mapper
public interface ShortUrlMapper extends BaseDao<ShortUrl> {

    /**
     * 根据查询条件查询短链接列表
     * @param shortUrl 查询条件
     * @return 列表
     */
    List<ShortUrlVO> selectShortUrlByCondition(ShortUrl shortUrl);

}
