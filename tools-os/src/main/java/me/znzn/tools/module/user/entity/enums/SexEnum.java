package me.znzn.tools.module.user.entity.enums;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
public enum SexEnum {

    /**
     * 男
     */
    MALE(0, "男"),

    /**
     * 女
     */
    FEMALE(1, "女"),

    /**
     * 未知
     */
    UNKNOWN(2, "未知");

    private Integer index;

    private String msg;

    SexEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer code) {
        for (SexEnum sexEnum : values()) {
            if (sexEnum.index.equals(code)) {
                return sexEnum.msg;
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
