package me.znzn.tools.module.url.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.url.service.ShortUrlStatisticsService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhuzening
 * @date 2019/11/11
 * @since 1.0
 */
@Controller
public class ShortUrlController {

    @Resource
    private ShortUrlService shortUrlService;
    @Resource
    private ShortUrlStatisticsService shortUrlStatisticsService;

    @RequestMapping("/url/save")
    @ResponseBody
    public Result save(@RequestBody ShortUrl shortUrl) {
        if (StringUtils.isEmpty(shortUrl.getOriginUrl())) {
            throw new BusinessException("网址url不可为空！");
        }
        if (!shortUrl.getOriginUrl().matches("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?")) {
            throw new BusinessException("网址url需以\"http\"或\"https\"开头！");
        }
        String token = LoginUserUtil.getToken();
        UserInfoVO loginUser = LoginUserUtil.getLoginUser(token);
        if (null != loginUser) {
            shortUrl.setCreateAccount(loginUser.getId());
        }
        return Result.success(shortUrlService.saveUrl(shortUrl));
    }

    @GetMapping("/url/query")
    @ResponseBody
    public Result query(ShortUrl shortUrl) {
        UserInfoVO loginUser = LoginUserUtil.getLoginUser();
        return Result.success(shortUrlService.getUserUrlList(loginUser, shortUrl));
    }

    @RequestMapping("/{shortUrl}**")
    public ModelAndView getOriginUrl(@PathVariable("shortUrl") String shortUrl, HttpServletRequest request) {
        ShortUrl url = shortUrlService.getOriginUrl(shortUrl);
        shortUrlStatisticsService.saveVisitHistory(shortUrl, request);
        if (null == url) {
            return new ModelAndView("404");
        }
        return new ModelAndView("redirect:" + url.getOriginUrl());
    }

}
