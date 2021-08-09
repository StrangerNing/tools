package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.common.component.MailSendParams;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.enums.EidStatusEnum;
import me.znzn.tools.module.blog.entity.enums.SubscribeEnableEnum;
import me.znzn.tools.module.blog.entity.po.Eid;
import me.znzn.tools.module.blog.entity.po.Subscribe;
import me.znzn.tools.module.blog.mapper.EidMapper;
import me.znzn.tools.module.blog.mapper.SubscribeMapper;
import me.znzn.tools.module.blog.service.SubscribeService;
import me.znzn.tools.utils.MailSenderUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public void enableSubscribe(String eid) {
        Eid dbEid = getEidBeanByEid(eid);
        String email = dbEid.getEmail();

        Subscribe querySubscribe = new Subscribe();
        querySubscribe.setMail(email);
        querySubscribe.setType(dbEid.getType());
        List<Subscribe> dbList = subscribeMapper.selectByProperty(querySubscribe);
        if (CollectionUtils.isEmpty(dbList)) {
            throw new BusinessException("申请记录失踪了，请返回重试");
        }
        Subscribe db = dbList.get(0);
        if (SubscribeEnableEnum.ENABLE.getIndex().equals(db.getEnable())) {
            return;
        }
        db.setEnable(SubscribeEnableEnum.ENABLE.getIndex());
        subscribeMapper.updateByPrimaryKey(db);
    }

    @Override
    public List<Subscribe> disableSubscribe(String eid) {
        Eid dbEid = getEidBeanByEid(eid);
        String mail = dbEid.getEmail();

        Subscribe query = new Subscribe();
        query.setMail(mail);
        
        return subscribeMapper.selectByProperty(query);
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
//        eidMapper.updateByPrimaryKey(dbEid);
        return dbEid;
    }
}
