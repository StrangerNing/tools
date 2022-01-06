package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/3
 */
public enum TimelineTypeEnum {

    /**
     * 绿色
     */
    SUCCESS(1, "bg-success"),

    /**
     * 蓝色
     */
    INFO(2, "bg-info"),

    /**
     * 黄色
     */
    WARNING(3, "bg-warning"),

    /**
     * 红色
     */
    DANGER(4, "bg-danger");

    private Integer index;

    private String msg;

    TimelineTypeEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (TimelineTypeEnum timelineTypeEnum : values()) {
            if (timelineTypeEnum.index.equals(index)) {
                return timelineTypeEnum.msg;
            }
        }
        return "未知的操作类型";
    }

    public Integer getIndex() {
        return index;
    }

    public String getMsg() {
        return msg;
    }
}
