package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/24
 */
public enum ArticleStatusEnum {
    /**
     * 冻结
     */
    NORMAL(1, "正常"),

    DRAFT(2, "草稿"),

    DELAY(3, "定时"),

    /**
     * 有效
     */
    DELETED(0, "删除");


    private Integer index;

    private String msg;

    ArticleStatusEnum(Integer index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public static String getMsg(Integer index) {
        for (ArticleStatusEnum articleStatusEnum : values()) {
            if (articleStatusEnum.index.equals(index)) {
                return articleStatusEnum.msg;
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
