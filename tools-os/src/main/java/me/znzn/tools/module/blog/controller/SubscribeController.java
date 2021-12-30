package me.znzn.tools.module.blog.controller;

import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.blog.entity.enums.SubscribeEnableEnum;
import me.znzn.tools.module.blog.entity.form.SubscribeManageForm;
import me.znzn.tools.module.blog.entity.po.Subscribe;
import me.znzn.tools.module.blog.service.SubscribeService;
import me.znzn.tools.utils.MailSenderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/8/5
 */
@Controller
@RequestMapping("/wapi/blog/subscribe")
@Slf4j
public class SubscribeController {

    @Resource
    private SubscribeService subscribeService;

    @PostMapping("/newsletter/check")
    @ResponseBody
    public ResponseEntity subscribe(@RequestBody Subscribe subscribe) {
        subscribe.setType(MailSenderUtil.MailTypeEnum.USER_NEWSLETTER.getType());
        subscribe.setEnable(SubscribeEnableEnum.DISABLE.getIndex());
        subscribeService.subscribe(subscribe);
        return ResultPageUtil.success();
    }

    @PostMapping("/manage")
    @ResponseBody
    public ResponseEntity manageSubscribe(@RequestBody SubscribeManageForm subscribeManageForm) {
        subscribeService.manageSubscribe(subscribeManageForm);
        return ResultPageUtil.success();
    }
}
