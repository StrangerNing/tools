package me.znzn.tools.common.enums;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
public enum OssBucketNameEnum {

    /**
     * 头像
     */
    AVATAR("avatar/"),

    /**
     * 图片
     */
    IMAGE("image/"),

    /**
     * 文件
     */
    FILE("file/"),

    /**
     * 其他
     */
    OTHERS("others/");

    private String name;

    public String getName() {
        return name;
    }

    OssBucketNameEnum(String name) {
        this.name = name;
    }

    public Boolean equals(OssBucketNameEnum ossBucketNameEnum) {
        return name.equals(ossBucketNameEnum.getName());
    }
}
