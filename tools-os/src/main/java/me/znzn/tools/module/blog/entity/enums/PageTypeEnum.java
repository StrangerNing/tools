package me.znzn.tools.module.blog.entity.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/10
 */
public enum PageTypeEnum {
    /**
     * 列表
     */
    LIST(1, "category-list", 5),

    /**
     * 块
     */
    GRID(2, "category-grid", 6),

    MASONRY(3, "category-masonry", 6),

    BIG(4, "category-big", 6);

    private Integer index;

    private Integer limit;

    private String name;

    PageTypeEnum(Integer index, String name, Integer limit) {
        this.index = index;
        this.name = name;
        this.limit = limit;
    }

    public static String getName(Integer index) {
        for (PageTypeEnum pageTypeEnum : values()) {
            if (pageTypeEnum.index.equals(index)) {
                return pageTypeEnum.name;
            }
        }
        return LIST.getName();
    }

    public static PageTypeEnum getPageTypeEnum(Integer index) {
        for (PageTypeEnum pageTypeEnum : values()) {
            if (pageTypeEnum.index.equals(index)) {
                return pageTypeEnum;
            }
        }
        return PageTypeEnum.LIST;
    }

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Integer getLimit() {
        return limit;
    }
}
