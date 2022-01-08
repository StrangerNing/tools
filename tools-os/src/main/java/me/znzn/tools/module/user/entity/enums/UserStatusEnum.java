package me.znzn.tools.module.user.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/7
 */
public enum UserStatusEnum {

    /**
     * 冻结
     */
    DISABLE(0, "冻结"),

    /**
     * 有效
     */
    ENABLE(1, "有效"),

    /**
     * 等待邮箱确认
     */
    WAIT_AUTH(2, "等待确认");

    private Integer index;

    private String msg;

    UserStatusEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (UserStatusEnum statusEnum : values()) {
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
