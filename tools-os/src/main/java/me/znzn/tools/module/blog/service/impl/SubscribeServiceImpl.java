package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.common.component.MailSendParams;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.enums.EidStatusEnum;
import me.znzn.tools.module.blog.entity.enums.SubscribeEnableEnum;
import me.znzn.tools.module.blog.entity.form.SubscribeManageForm;
import me.znzn.tools.module.blog.entity.po.Eid;
import me.znzn.tools.module.blog.entity.po.Subscribe;
import me.znzn.tools.module.blog.mapper.EidMapper;
import me.znzn.tools.module.blog.mapper.SubscribeMapper;
import me.znzn.tools.module.blog.service.SubscribeService;
import me.znzn.tools.utils.MailSenderUtil;
import me.znzn.tools.utils.ValidatorUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/8/5
 */
@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Resource
    private SubscribeMapper subscribeMapper;
    @Resource
    private EidMapper eidMapper;
    @Resource
    private MailSenderUtil mailSenderUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscribe(Subscribe subscribe) {
        subscribe.setCreateTime(new Date());

        Subscribe query = new Subscribe();
        query.setMail(subscribe.getMail());
        query.setType(subscribe.getType());
        List<Subscribe> subscribes = subscribeMapper.selectByProperty(query);
        if (CollectionUtils.isEmpty(subscribes)) {
            subscribeMapper.insertByProperty(subscribe);
        } else {
            for (Subscribe db: subscribes) {
                if (SubscribeEnableEnum.ENABLE.getIndex().equals(db.getEnable())) {
                    throw new BusinessException("此邮箱已激活");
                }
            }
        }

        MailSendParams params = new MailSendParams();
        params.setTo(subscribe.getMail());
        params.setNickname(subscribe.getMail());
        mailSenderUtil.send(params, MailSenderUtil.MailTypeEnum.USER_SUBSCRIBE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String enableSubscribe(String eid) {
        return modifySubscribeStatus(eid, SubscribeEnableEnum.ENABLE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String disableSubscribe(String eid) {
        return modifySubscribeStatus(eid, SubscribeEnableEnum.DISABLE);
    }

    @Override
    public void manageSubscribe(SubscribeManageForm subscribeManageForm) {
        ValidatorUtil.validate(subscribeManageForm);
        Eid eidQuery = new Eid();
        eidQuery.setEid(subscribeManageForm.getEid());
        List<Eid> eids = eidMapper.selectByProperty(eidQuery);
        if (CollectionUtils.isEmpty(eids)) {
            throw new BusinessException("很抱歉我忘记你是谁了，请返回重试");
        }
        Eid dbEid = eids.get(0);
        String email = dbEid.getEmail();

        Subscribe query = new Subscribe();
        query.setMail(email);
        subscribeMapper.deleteByProperty(query);

        List<Integer> manageList = subscribeManageForm.getSubscribeTypes();
        List<Subscribe> subscribes = new ArrayList<>();

        for (MailSenderUtil.MailNeedSubscribeEnum type : MailSenderUtil.MailNeedSubscribeEnum.values()) {
            if (manageList.contains(type.getType())) {
                if (type.getNeedSubscribe()) {
                    Subscribe subscribe = new Subscribe();
                    subscribe.setMail(email);
                    subscribe.setType(type.getType());
                    subscribe.setEnable(SubscribeEnableEnum.ENABLE.getIndex());
                    subscribe.setCreateTime(new Date());
                    subscribes.add(subscribe);
                }
            } else {
                if (!type.getNeedSubscribe()) {
                    Subscribe subscribe = new Subscribe();
                    subscribe.setMail(email);
                    subscribe.setType(type.getType());
                    subscribe.setEnable(SubscribeEnableEnum.DISABLE.getIndex());
                    subscribe.setCreateTime(new Date());
                    subscribes.add(subscribe);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(subscribes)) {
            subscribeMapper.insertBatchByProperty(subscribes);
        }
    }

    @Override
    public List<Integer> getSubscribeList(String mailAddress) {
        Subscribe query = new Subscribe();
        query.setMail(mailAddress);
        List<Subscribe> subscribes = subscribeMapper.selectByProperty(query);
        Map<Integer, Integer> subscribe = new HashSet<>(subscribes).stream().collect(Collectors.toMap(Subscribe::getType, Subscribe::getEnable));
        List<Integer> subscribeList = new ArrayList<>();
        for (MailSenderUtil.MailNeedSubscribeEnum type : MailSenderUtil.MailNeedSubscribeEnum.values()) {
            Integer isSubscribe = subscribe.get(type.getType());
            if (!type.getNeedSubscribe() && isSubscribe == null) {
                subscribeList.add(type.getType());
                continue;
            }
            if (SubscribeEnableEnum.ENABLE.getIndex().equals(isSubscribe)) {
                subscribeList.add(type.getType());
            }
        }
        return subscribeList;
    }

    private String modifySubscribeStatus(String eid, SubscribeEnableEnum subscribeEnableEnum) {
        Eid dbEid = getEidBeanByEid(eid);
        String email = dbEid.getEmail();

        Subscribe querySubscribe = new Subscribe();
        querySubscribe.setMail(email);
        querySubscribe.setType(dbEid.getType());
        List<Subscribe> dbList = subscribeMapper.selectByProperty(querySubscribe);
        if (CollectionUtils.isEmpty(dbList)) {
            querySubscribe.setEnable(subscribeEnableEnum.getIndex());
            querySubscribe.setCreateTime(new Date());
            subscribeMapper.insertByProperty(querySubscribe);
            return email;
        }

        Subscribe db = dbList.get(0);
        if (subscribeEnableEnum.getIndex().equals(db.getEnable())) {
            return email;
        }
        db.setEnable(subscribeEnableEnum.getIndex());
        subscribeMapper.updateByPrimaryKey(db);
        return email;
    }

    private Eid getEidBeanByEid(String eid) {
        if (StringUtils.isEmpty(eid)) {
            throw new BusinessException("链接出错啦，请返回重试");
        }
        Eid query = new Eid();
        query.setEid(eid);
        query.setStatus(EidStatusEnum.ENABLE.getIndex());
        List<Eid> eids = eidMapper.selectByProperty(query);
        if (CollectionUtils.isEmpty(eids)) {
            throw new BusinessException("很抱歉我忘记你是谁了，请返回重试");
        }
        Eid dbEid = eids.get(0);
        dbEid.setStatus(EidStatusEnum.DISABLE.getIndex());
        eidMapper.updateByPrimaryKey(dbEid);
        return dbEid;
    }
}
