package me.znzn.tools.module.blog.entity.vo;

import me.znzn.tools.module.blog.entity.enums.TimelineTypeEnum;
import me.znzn.tools.module.blog.entity.po.Timeline;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/4
 */
public class TimelineVo extends Timeline {

    private String typeBg;

    public String getTypeBg() {
        return TimelineTypeEnum.getMsg(getType());
    }
}
