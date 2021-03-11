package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/11
 */
public enum ArticleTypeEnum {

    /**
     * 默认
     */
    DEFAULT(1, "single"),

    /**
     * 大图
     */
    BIG(2, "single-2"),

    LEFT(3, "single-3"),

    SIDEBAR(4, "single-4"),

    IMAGES(5, "single-5");

    private Integer index;

    private String name;

    ArticleTypeEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    public static String getMsg(Integer index) {
        for (ArticleTypeEnum articleTypeEnum : values()) {
            if (articleTypeEnum.index.equals(index)) {
                return articleTypeEnum.name;
            }
        }
        return DEFAULT.name;
    }

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
