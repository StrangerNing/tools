package me.znzn.tools.module.blog.mapper;

import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.blog.entity.po.Timeline;
import me.znzn.tools.module.blog.entity.vo.TimelineVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据接口
 *
 * @author
 * @version 1.0.0
 * @date 2022/01/02 01:36:28
 * @copyright
 */
@Mapper
public interface TimelineMapper extends BaseDao<Timeline> {

    List<TimelineVo> getTimelineVoByProperty(Timeline timeline);
}
