package me.znzn.tools.module.blog.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.MailSendParams;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.enums.FriendsLinkStatusEnum;
import me.znzn.tools.module.blog.entity.po.Friends;
import me.znzn.tools.module.blog.mapper.FriendsMapper;
import me.znzn.tools.module.blog.service.FriendsService;
import me.znzn.tools.utils.MailSenderUtil;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/6/25
 */
@Slf4j
@Service
public class FriendsServiceImpl implements FriendsService {

    @Resource
    private FriendsMapper friendsMapper;
    @Resource
    private MailSenderUtil mailSenderUtil;

    @Override
    public List<Friends> getFriendsList(Friends friends) {
        return friendsMapper.selectByProperty(friends);
    }

    @Override
    public Integer countFriendsList(Friends friends) {
        return friendsMapper.countByProperty(friends);
    }

    @Override
    public void addFriendsLink(Friends friends) {
        if (StringUtils.isEmpty(friends.getName())) {
            throw new BusinessException("网站名称不能为空");
        }
        if (StringUtils.isEmpty(friends.getWebsite())) {
            throw new BusinessException("网站地址不能为空");
        }
        if (StringUtils.isEmpty(friends.getIcon())) {
            throw new BusinessException("网站图标不能为空");
        }
        if (StringUtils.isEmpty(friends.getIntroduction())) {
            throw new BusinessException("网站介绍不能为空");
        }
        if (friends.getStatus() == null) {
            throw new BusinessException("友链状态不能为空");
        }
        friendsMapper.insertByProperty(friends);
    }

    @Override
    public void updateFriendsLink(Friends friends) {
        if (!StringUtils.isEmpty(friends.getEmail())) {
            MailSendParams params = new MailSendParams();
            params.setTo(friends.getEmail());
            params.setNickname(StringUtils.isEmpty(friends.getNickname()) ? friends.getWebsite() : friends.getNickname());
            if (FriendsLinkStatusEnum.APPROVE.getIndex().equals(friends.getStatus())) {
                mailSenderUtil.send(params, MailSenderUtil.MailTypeEnum.FRIEND_CONFIRM);
            } else if (FriendsLinkStatusEnum.INVALIDATE.getIndex().equals(friends.getStatus())
                    || FriendsLinkStatusEnum.REMOVED.getIndex().equals(friends.getStatus())) {
                params.setCancelReason(FriendsLinkStatusEnum.getMsg(friends.getStatus()));
                mailSenderUtil.send(params, MailSenderUtil.MailTypeEnum.FRIEND_CANCEL);
            } else if (FriendsLinkStatusEnum.DENY.getIndex().equals(friends.getStatus())) {
                params.setCancelReason(friends.getMessage());
                mailSenderUtil.send(params, MailSenderUtil.MailTypeEnum.FRIEND_CANCEL);
            }
        }
        friendsMapper.updateByPrimaryKey(friends);
    }
}
