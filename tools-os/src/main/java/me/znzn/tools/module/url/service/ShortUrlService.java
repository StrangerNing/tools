package me.znzn.tools.module.url.service;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

/**
 * @author zhuzening
 * @date 2019/11/11
 * @since 1.0
 */
public interface ShortUrlService {

    /**
     * 保存url，返回短链接
     * @param url 原链接
     * @return java.lang.String 短网址
     */
    String saveUrl(ShortUrl url);

    /**
     * 根据短链接获取原链接
     * @param shortUrl 短链接
     * @return me.znzn.tools.module.url.entity.po.ShortUrl 原链接
     */
    ShortUrl getOriginUrl(String shortUrl);

    /**
     * 获取用户的短链接列表
     * @param userInfoVO 登陆用户
     * @param shortUrl 分页
     * @return me.znzn.tools.common.component.ResultPage 断链列表
     */
    ResultPage getUserUrlList(UserInfoVO userInfoVO, ShortUrl shortUrl);

    /**
     * 更新短链接状态
     * @param shortUrl 更新项
     * @return java.lang.Boolean 更新结果
     */
    Boolean updateShortUrlStatus(ShortUrl shortUrl);

}
