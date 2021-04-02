package me.znzn.tools.common.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
public enum OssFileTypeEnum {

    /**
     * 头像
     */
    AVATAR("avatar/", 1),

    /**
     * 图片
     */
    IMAGE("image/", 2),

    /**
     * 文件
     */
    FILE("file/", 3),

    /**
     * 博客
     */
    BLOG("blog/", 4),

    /**
     * 其他
     */
    OTHERS("others/", 5),

    /**
     * instagram
     */
    INSTAGRAM("ig/", 6);

    private String name;

    private Integer index;

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    OssFileTypeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public Boolean equals(OssFileTypeEnum ossFileTypeEnum) {
        return name.equals(ossFileTypeEnum.getName());
    }
}
