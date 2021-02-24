package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/24
 */
public enum ArticlePermissionEnum {
    /**
     * 冻结
     */
    ALL(1, "所有人"),

    LOGIN(2, "仅登录"),

    /**
     * 有效
     */
    PRIVATE(3, "私人");


    private Integer index;

    private String msg;

    ArticlePermissionEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (ArticlePermissionEnum articlePermissionEnum : values()) {
            if (articlePermissionEnum.index.equals(index)) {
                return articlePermissionEnum.msg;
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
