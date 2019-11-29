package me.znzn.tools.module.url.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhuzening
 * @date 2019/11/28
 * @since 1.0
 */
public interface ShortUrlStatisticsService {

    /**
     * 保存访问记录
     * @param shortUrl 短链接
     * @param request 访问请求
     */
    void saveVisitHistory(String shortUrl, HttpServletRequest request);

}
