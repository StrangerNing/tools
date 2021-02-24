package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/22
 */
public enum EditTypeEnum {
    /**
     * 冻结
     */
    MARKDOWN(1, "markdown"),

    /**
     * 有效
     */
    HTML(2, "html");

    private Integer index;

    private String msg;

    EditTypeEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (EditTypeEnum editTypeEnum : values()) {
            if (editTypeEnum.index.equals(index)) {
                return editTypeEnum.msg;
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
