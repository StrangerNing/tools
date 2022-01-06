package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.map.MapUtil;
import me.znzn.tools.module.blog.entity.po.Timeline;
import me.znzn.tools.module.blog.entity.vo.TimelineVo;
import me.znzn.tools.module.blog.mapper.TimelineMapper;
import me.znzn.tools.module.blog.service.TimelineService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/2
 */
@Service
public class TimelineServiceImpl implements TimelineService {

    @Resource
    private TimelineMapper timelineMapper;

    @Override
    public List<TimelineVo> getTimelineList(Timeline timeline) {
        List<TimelineVo> timelines = timelineMapper.getTimelineVoByProperty(timeline);
        return timelines;
    }

    @Override
    public Map<Integer, List<TimelineVo>> getTimelineByYear(Timeline timeline) {
        List<TimelineVo> timelineList = getTimelineList(timeline);
        Map<Integer, List<TimelineVo>> map = new TreeMap<>();
        if (CollectionUtils.isNotEmpty(timelineList)) {
            ZoneId timeZone = ZoneId.systemDefault();
            map = timelineList.stream().collect(Collectors.groupingBy(timelineVo -> timelineVo.getPublishTime().toInstant().atZone(timeZone).getYear()));
        }
        MapUtil.sort(map, (o1, o2) -> o2 - o1);
        return map;
    }

    @Override
    public Integer countTimelineList(Timeline timeline) {
        return timelineMapper.countByProperty(timeline);
    }

    @Override
    public void addEvent(Timeline timeline) {
        timelineMapper.insertByProperty(timeline);
    }

    @Override
    public void updateEvent(Timeline timeline) {
        timelineMapper.updateByPrimaryKey(timeline);
    }
}
