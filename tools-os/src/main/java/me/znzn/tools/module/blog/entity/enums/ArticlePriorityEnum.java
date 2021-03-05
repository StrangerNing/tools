package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/4
 */
public enum ArticlePriorityEnum {
    /**
     * 冻结
     */
    DOWN(1, "下沉"),

    NORMAL(10, "正常"),

    /**
     * 有效
     */
    TOP(100, "置顶");


    private Integer index;

    private String msg;

    ArticlePriorityEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (ArticlePriorityEnum articlePriorityEnum : values()) {
            if (articlePriorityEnum.index.equals(index)) {
                return articlePriorityEnum.msg;
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
