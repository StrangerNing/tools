package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/24
 */
public enum CommentLimitEnum {
    /**
     * 冻结
     */
    ENABLE(1, "允许"),

    DISABLE(0, "不允许");


    private Integer index;

    private String msg;

    CommentLimitEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (CommentLimitEnum commentLimitEnum : values()) {
            if (commentLimitEnum.index.equals(index)) {
                return commentLimitEnum.msg;
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
