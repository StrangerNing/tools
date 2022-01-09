package me.znzn.tools.common.constant;

/**
 * @author zhuzening
 * @date 2019/11/27
 * @since 1.0
 */
public class CommonConstant {

    /**
     * 后台域名地址
     */
    public static String BACKGROUND_DOMAIN = "";

    public static String SHORT_URL_PREFIX = "";

    /**
     * 百度地图AK
     */
    public static String MAP_AK = "";

    public static String MAP_REQUEST_PREFIX = "";

    /**
     * 有效的访问间隔
     */
    public static String VALID_VISIT_INTERVAL = "";

    /**
     * 验证码后端验证地址
     */
    public static String CAPTCHA_END_URL = "https://recaptcha.net/recaptcha/api/siteverify";

    /**
     * 验证码密钥
     */
    public static String CAPTCHA_SECRET = "";

    /**
     * 短链接缓存时间
     */
    public static String SHORT_URL_CACHE_TIME = "10";

    /**
     * 文件缓存时间
     */
    public static String FILE_CACHE_TIME = "1";


    /**
     * 阿里云OSS相关配置
     */
    public static String OSS_ENDPOINT = "";

    public static String OSS_ACCESS_KEY_ID = "";

    public static String OSS_ACCESS_KEY_SECRET = "";

    public static String OSS_BUCKET_NAME = "";

    public static String OSS_URL_EXPIRATION = "3600000";

    /**
     * 文件访问后台地址前缀
     */
    public static String FILE_REQUEST_PREFIX = "";

    public static String ARTICLE_URL_PREFIX = "/posts/";

    public static String CATEGORY_URL_PREFIX = "/category/";

    /**
     * 有效的阅读量间隔
     */
    public static String VALID_ARTICLE_VIEW_INTERVAL = "10800000";

    /**
     * 有效的友链申请间隔
     */
    public static String VALID_APPLY_FRIENDS_INTERVAL = "21600000";

    public static String SSO_URL = "";

    public static String BLOG_LOGIN_URL = "";

    /**
     * 阿里云邮件回信地址，用","或者";"分隔
     */
    public static String MAIL_REPLY_TO = "";

    public static String GOOGLE_VERIFY = "https://oauth2.googleapis.com/tokeninfo";
}
