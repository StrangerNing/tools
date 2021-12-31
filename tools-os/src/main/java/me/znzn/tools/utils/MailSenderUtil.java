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
    private SubscribeMapper subscribeMapper;

    public enum MailTypeEnum {

        /**
         * 友链确认
         */
        FRIEND_CONFIRM("friend-confirm", "您的网站已被确认添加进友链", "notify", 1, false),

        /**
         * 友链取消
         */
        FRIEND_CANCEL("friend-cancel", "友链即将被取消", "notify", 1, false),

        /**
         * 友链审核不通过
         */
        FRIEND_UNSUCCESSFUL("friend-unsuccessful", "友链申请未通过", "notify", 1, false),

        /**
         * 消息提醒
         */
        USER_MESSAGE("user-message", "您有一条新的消息提醒", "notify", 2, false),

        /**
         * 订阅提醒
         */
        USER_SUBSCRIBE("user-subscribe", "确认订阅提醒", "notify", 3, false),

        /**
         * 订阅消息
         */
        USER_NEWSLETTER("user-newsletter", "有新的文章发布", "subscribe", 4, true);

        private String template;

        private String subject;

        private String sender;

        private Integer type;

        private Boolean needSubscribe;

        MailTypeEnum(String template, String subject, String sender, Integer type, Boolean needSubscribe) {
            this.template = template;
            this.subject = subject;
            this.sender = sender;
            this.type = type;
            this.needSubscribe = needSubscribe;
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

        public Integer getType() {
            return type;
        }

        public Boolean getNeedSubscribe() {
            return needSubscribe;
        }

        /**
         * 是否是无需订阅即可发送的类型
         * @param type
         * @return
         */
        public static Boolean needSubscribe(Integer type) {
            if (type == null) {
                return true;
            }
            MailTypeEnum[] values = MailTypeEnum.values();
            for (MailTypeEnum value : values) {
                if (value.getType().equals(type)) {
                    return value.getNeedSubscribe();
                }
            }
            return true;
        }
    }

    public enum MailNeedSubscribeEnum {

        /**
         * 友链相关
         */
        FRIEND("友链确认、取消通知", 1, false),

        /**
         * 评论相关
         */
        COMMENT("评论通知", 2, false),

        /**
         * 确认订阅
         */
        CONFIRM_SUBSCRIBE("确认订阅通知", 3, false),

        /**
         * 最新文章
         */
        NEWSLETTER("最新文章推送", 4, true);

        private String title;

        private Integer type;

        private Boolean needSubscribe;

        public String getTitle() {
            return title;
        }

        public Integer getType() {
            return type;
        }

        public Boolean getNeedSubscribe() {
            return needSubscribe;
        }

        public static Boolean getNeedSubscribe(Integer type) {
            if (type == null) {
                return false;
            }
            for (MailNeedSubscribeEnum value : MailNeedSubscribeEnum.values()) {
                if (value.type.equals(type)) {
                    return value.needSubscribe;
                }
            }
            return false;
        }

        MailNeedSubscribeEnum(String title, Integer type, Boolean needSubscribe) {
            this.title = title;
            this.type = type;
            this.needSubscribe = needSubscribe;
        }
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void send(MailSendParams params, MailTypeEnum type) {
        if (StringUtils.isEmpty(params.getTo())) {
            return;
        }
        Subscribe subscribe = new Subscribe();
        subscribe.setMail(params.getTo());
        subscribe.setType(type.getType());
        subscribe.setEnable(SubscribeEnableEnum.DISABLE.getIndex());
        List<Subscribe> unsubscribeList = subscribeMapper.selectByProperty(subscribe);
        if (CollectionUtil.isNotEmpty(unsubscribeList)) {
            log.error("邮箱{}，邮件类型{}，已退订此消息", params.getTo(), type.getType());
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
        eid.setType(type.getType());
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
