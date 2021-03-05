package me.znzn.tools.module.api.service;

import me.znzn.tools.module.api.entity.GenerateShortUrlVO;

import java.util.Map;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/4/21
 */
public interface ApiService {

    /**
     * 获取短链接
     * @param shortUrlVO api key
     * @return 短链接
     */
    String getShortUrl(GenerateShortUrlVO shortUrlVO);

    /**
     * 上传文件
     * @param file
     * @return
     */
    String uploadFile(Map<String, String> file);
}
