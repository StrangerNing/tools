package me.znzn.tools.service;

/**
 * @author zhuzening
 * @date 2019/11/11
 * @since 1.0
 */
public interface ShortUrlService {

    /**
     * 保存url，返回短链接
     * @param url 原链接
     * @return 短网址
     */
    String saveUrl(String url);

    /**
     * 根据短链接获取原链接
     * @param shortUrl 短链接
     * @return 原链接
     */
    String getOriginUrl(String shortUrl);
}
