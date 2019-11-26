package me.znzn.tools.module.url.service;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.entity.vo.ShortUrlVO;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

import java.util.List;

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
    String saveUrl(ShortUrl url);

    /**
     * 根据短链接获取原链接
     * @param shortUrl 短链接
     * @return 原链接
     */
    ShortUrl getOriginUrl(String shortUrl);

    /**
     * 获取用户的短链接列表
     * @param userInfoVO 登陆用户
     * @param shortUrl 分页
     * @return 断链列表
     */
    ResultPage getUserUrlList(UserInfoVO userInfoVO, ShortUrl shortUrl);

}
