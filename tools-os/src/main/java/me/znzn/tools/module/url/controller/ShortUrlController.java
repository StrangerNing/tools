package me.znzn.tools.module.url.controller;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.url.entity.form.VisitHisForm;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.url.service.ShortUrlStatisticsService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/wapi")
public class ShortUrlController {

    @Resource
    private ShortUrlService shortUrlService;
    @Resource
    private ShortUrlStatisticsService shortUrlStatisticsService;

    @RequestMapping("/url/save")
    @ResponseBody
    public ResponseEntity save(@RequestBody ShortUrl shortUrl) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        if (null != loginUser) {
            shortUrl.setCreateAccount(loginUser.getId());
        }
        return ResultPageUtil.success(shortUrlService.saveUrl(shortUrl));
    }

    @GetMapping("/url/query")
    @ResponseBody
    public ResponseEntity query(ShortUrl shortUrl) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        ResultPage page = shortUrlService.getUserUrlList(loginUser, shortUrl);
        return ResultPageUtil.successWithPage(page.getList(), page.getTotalCount(), page.getCurrentPage());
    }

    @PostMapping("/url/update")
    @ResponseBody
    public ResponseEntity updateShortUrl(@RequestBody ShortUrl shortUrl) {
        return ResultPageUtil.success(shortUrlService.updateShortUrlStatus(shortUrl));
    }

    @GetMapping("/url/statistics")
    @ResponseBody
    public ResponseEntity getUrlStatistics(VisitHisForm visitHisForm) {
        return ResultPageUtil.success(shortUrlStatisticsService.getVisitHistoryListByUrlId(visitHisForm));
    }

}
