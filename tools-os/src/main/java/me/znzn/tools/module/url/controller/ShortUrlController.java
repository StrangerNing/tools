package me.znzn.tools.module.url.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.module.url.entity.form.VisitHisForm;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.url.service.ShortUrlStatisticsService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
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
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        if (null != loginUser) {
            shortUrl.setCreateAccount(loginUser.getId());
        }
        return Result.success(shortUrlService.saveUrl(shortUrl));
    }

    @GetMapping("/url/query")
    @ResponseBody
    public Result query(ShortUrl shortUrl) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return Result.success(shortUrlService.getUserUrlList(loginUser, shortUrl));
    }

    @RequestMapping("/{shortUrl}**")
    public ModelAndView getOriginUrl(@PathVariable("shortUrl") String shortUrl, HttpServletRequest request) {
        ShortUrl url = shortUrlService.getOriginUrl(shortUrl);
        if (null == url || url.getStatus().equals(StatusEnum.DISABLE.getIndex())) {
            return new ModelAndView("404");
        }
        shortUrlStatisticsService.saveVisitHistory(shortUrl, request);
        return new ModelAndView("redirect:" + url.getOriginUrl());
    }

    @PostMapping("/url/update")
    @ResponseBody
    public Result updateShortUrl(@RequestBody ShortUrl shortUrl) {
        return Result.success(shortUrlService.updateShortUrlStatus(shortUrl));
    }

    @GetMapping("/url/statistics")
    @ResponseBody
    public Result getUrlStatistics(VisitHisForm visitHisForm) {
        return Result.success(shortUrlStatisticsService.getVisitHistoryListByUrlId(visitHisForm));
    }

}
