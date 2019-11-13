package me.znzn.tools.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.entity.ShortUrl;
import me.znzn.tools.service.ShortUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @date 2019/11/11
 * @since 1.0
 */
@Controller
public class ShortServiceController {

    @Resource
    private ShortUrlService shortUrlService;

    @RequestMapping("shortUrl")
    public String shortUrl() {
        return "shortUrl";
    }

    @RequestMapping("save")
    @ResponseBody
    public Result save(@RequestBody ShortUrl shortUrl) {
        if (StringUtils.isEmpty(shortUrl.getOriginUrl())) {
            throw new BusinessException("网址url不可为空！");
        }
        if (!shortUrl.getOriginUrl().matches("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?")) {
            throw new BusinessException("网址url需以\"http\"或\"https\"开头！");
        }
        return Result.success(shortUrlService.saveUrl(shortUrl.getOriginUrl()));
    }

    @RequestMapping("{shortUrl}")
    public ModelAndView getOriginUrl(@PathVariable("shortUrl") String shortUrl) {
        String originUrl = shortUrlService.getOriginUrl(shortUrl);
        return new ModelAndView("redirect:"+originUrl);
    }
}
