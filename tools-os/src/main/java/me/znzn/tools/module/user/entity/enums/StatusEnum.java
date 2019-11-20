package me.znzn.tools.module.user.entity.enums;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
public enum StatusEnum {

    /**
     * 冻结
     */
    DISABLE(0, "冻结"),

    /**
     * 有效
     */
    ENABLE(1, "有效");

    private Integer index;

    private String msg;

    StatusEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.index.equals(index)) {
                return statusEnum.msg;
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
