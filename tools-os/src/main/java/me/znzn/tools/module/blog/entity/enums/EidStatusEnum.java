package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/8/6
 */
public enum EidStatusEnum {
    /**
     * 有效
     */
    ENABLE(1, "有效"),

    /**
     * 冻结
     */
    DISABLE(0, "冻结");

    private Integer index;

    private String msg;

    EidStatusEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (EidStatusEnum eidStatusEnum : values()) {
            if (eidStatusEnum.index.equals(index)) {
                return eidStatusEnum.msg;
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
