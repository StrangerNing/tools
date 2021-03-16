package me.znzn.tools.module.url.controller;

import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.url.service.ShortUrlStatisticsService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/17
 */
@Controller
public class GetUrlController {
    @Resource
    private ShortUrlService shortUrlService;

    @Resource
    private ShortUrlStatisticsService shortUrlStatisticsService;

    @RequestMapping("/getUrl/{shortUrl}")
    public ModelAndView getOriginUrl(@PathVariable("shortUrl") String shortUrl, HttpServletRequest request) {
        ShortUrl url = shortUrlService.getOriginUrl(shortUrl);
        if (null == url || url.getStatus().equals(StatusEnum.DISABLE.getIndex())) {
            return new ModelAndView("/dwz/404");
        }
        shortUrlStatisticsService.saveVisitHistory(shortUrl, request);
        String queryString = request.getQueryString();
        if (null == queryString) {
            queryString = "";
        }
        String originUrl = "";
        if (url.getOriginUrl() != null) {
            if (url.getOriginUrl().contains("?")) {
                originUrl = url.getOriginUrl().concat("&").concat(queryString);
            } else {
                originUrl = url.getOriginUrl().concat("?").concat(queryString);
            }
        }
        return new ModelAndView("redirect:" + originUrl);
    }
}
