package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.po.Timeline;
import me.znzn.tools.module.blog.entity.vo.TimelineVo;

import java.util.List;
import java.util.Map;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/2
 */
public interface TimelineService {

    /**
     * 获取时间线列表
     * @param timeline
     * @return
     */
    List<TimelineVo> getTimelineList(Timeline timeline);

    /**
     * 获取按年份分类的时间线
     * @param timeline
     * @return
     */
    Map<Integer, List<TimelineVo>> getTimelineByYear(Timeline timeline);

    /**
     * 获取事件数
     * @param timeline
     * @return
     */
    Integer countTimelineList(Timeline timeline);

    /**
     * 添加事件
     * @param timeline
     */
    void addEvent(Timeline timeline);

    /**
     * 修改事件
     * @param timeline
     */
    void updateEvent(Timeline timeline);

}
