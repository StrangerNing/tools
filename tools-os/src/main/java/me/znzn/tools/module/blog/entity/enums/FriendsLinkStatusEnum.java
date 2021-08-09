package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/6/24
 */
public enum FriendsLinkStatusEnum {
    /**
     * 冻结
     */
    WAIT(100, "待审核"),

    APPROVE(200, "已通过"),

    /**
     * 有效
     */
    INVALIDATE(301, "链接失效"),

    REMOVED(302, "本站被移除"),

    REPEAT(400, "重复申请"),

    DENY(500, "审核不通过");


    private Integer index;

    private String msg;

    FriendsLinkStatusEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (FriendsLinkStatusEnum friendsLinkStatusEnum : values()) {
            if (friendsLinkStatusEnum.index.equals(index)) {
                return friendsLinkStatusEnum.msg;
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
