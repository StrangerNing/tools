package me.znzn.tools.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.MailSendParams;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.blog.entity.enums.EidStatusEnum;
import me.znzn.tools.module.blog.entity.enums.SubscribeEnableEnum;
import me.znzn.tools.module.blog.entity.po.Eid;
import me.znzn.tools.module.blog.entity.po.Subscribe;
import me.znzn.tools.module.blog.entity.po.Unsubscribe;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.EidMapper;
import me.znzn.tools.module.blog.mapper.SubscribeMapper;
import me.znzn.tools.module.blog.mapper.UnsubscribeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/8/2
 */
@Component
@Slf4j
public class MailSenderUtil {

    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private Environment environment;
    @Resource
    private EidMapper eidMapper;
    @Resource
    private UnsubscribeMapper unsubscribeMapper;
    @Resource
    private SubscribeMapper subscribeMapper;

    public enum MailTypeEnum {

        /**
         * 友链确认
         */
        FRIEND_CONFIRM("friend-confirm", "您的网站已被确认添加进友链", "notify", 1),

        /**
         * 友链取消
         */
        FRIEND_CANCEL("friend-cancel", "友链即将被取消", "notify", 1),

        /**
         * 消息提醒
         */
        USER_MESSAGE("user-message", "您有一条新的消息提醒", "notify", 2),

        /**
         * 订阅提醒
         */
        USER_SUBSCRIBE("user-subscribe", "确认订阅提醒", "notify", 3),

        /**
         * 订阅消息
         */
        USER_NEWSLETTER("user-newsletter", "有新的文章发布", "subscribe", 4);

        private String template;

        private String subject;

        private String sender;

        private Integer cls;

        MailTypeEnum(String template, String subject, String sender, Integer cls) {
            this.template = template;
            this.subject = subject;
            this.sender = sender;
            this.cls = cls;
        }

        public String getTemplate() {
            return template;
        }

        public String getSender() {
            return sender;
        }

        public String getSubject() {
            return subject;
        }

        public Integer getCls() {
            return cls;
        }
    }

    @Async
    public void send(MailSendParams params, MailTypeEnum type) {
        if (StringUtils.isEmpty(params.getTo())) {
            return;
        }
        Subscribe subscribe = new Subscribe();
        subscribe.setMail(params.getTo());
        subscribe.setType(type.getCls());
        subscribe.setEnable(SubscribeEnableEnum.DISABLE.getIndex());
        List<Subscribe> unsubscribeList = subscribeMapper.selectByProperty(subscribe);
        if (CollectionUtil.isNotEmpty(unsubscribeList)) {
            log.error("邮箱{}，邮件类型{}，已退订此消息", params.getTo(), type.getCls());
            return;
        }

        UUID uuid = UUID.randomUUID();
        params.setEid(uuid.toString());
        Context context = new Context();
        context.setVariables(BeanUtil.beanToMap(params));
        String content = templateEngine.process("emails/" + type.getTemplate(), context);
        MailAccount mailAccount = new MailAccount(environment.getRequiredProperty("mail.setting-path." + type.getSender()));
        Mail mail = Mail.create(mailAccount)
                .setUseGlobalSession(false)
                .setTos(params.getTo())
                .setTitle(type.getSubject())
                .setContent(content, true);
        List<String> replyTo = splitAddress(CommonConstant.MAIL_REPLY_TO);
        if (CollectionUtils.isNotEmpty(replyTo)) {
            mail.setReply(replyTo.toArray(new String[0]));
        }
        mail.send();

        Eid eid = new Eid();
        eid.setEid(uuid.toString());
        eid.setEmail(params.getTo());
        eid.setNickname(params.getNickname());
        eid.setTemplate(type.getTemplate());
        eid.setType(type.getCls());
        eid.setCreateTime(new Date());
        eid.setStatus(EidStatusEnum.ENABLE.getIndex());
        eidMapper.insertByProperty(eid);
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void sendSubscribe(ArticleVo articleVo) {
        Subscribe subscribe = new Subscribe();
        subscribe.setEnable(SubscribeEnableEnum.ENABLE.getIndex());
        List<Subscribe> subscribeList = subscribeMapper.selectByProperty(subscribe);
        List<String> content = articleVo.getPartContent();
        subscribeList.forEach(item -> {
            MailSendParams params = new MailSendParams();
            params.setTo(item.getMail());
            params.setNickname(articleVo.getAuthor());
            params.setMinutes(articleVo.getMinutes());
            params.setTitle(articleVo.getTitle());
            params.setContentList(content);
            params.setHref(CommonConstant.BACKGROUND_DOMAIN + articleVo.getAliasHref());
            SpringUtil.getBean(this.getClass()).send(params, MailTypeEnum.USER_NEWSLETTER);
        });
    }

    private static List<String> splitAddress(String addresses) {
        if (StrUtil.isBlank(addresses)) {
            return null;
        }

        List<String> result;
        if (StrUtil.contains(addresses, ',')) {
            result = StrUtil.splitTrim(addresses, ',');
        } else if (StrUtil.contains(addresses, ';')) {
            result = StrUtil.splitTrim(addresses, ';');
        } else {
            result = CollUtil.newArrayList(addresses);
        }
        return result;
    }
}
