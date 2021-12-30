package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.form.SubscribeManageForm;
import me.znzn.tools.module.blog.entity.po.Subscribe;

import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/8/5
 */
public interface SubscribeService {

    /**
     * 订阅
     * @param subscribe
     */
    void subscribe(Subscribe subscribe);

    /**
     * 修改订阅状态
     * @param eid
     * @return String 邮箱地址
     */
    String enableSubscribe(String eid);

    /**
     * 根据eid取消订阅
     * @param eid
     * @return String 邮箱地址
     */
    String disableSubscribe(String eid);

    /**
     * 管理订阅
     * @param subscribeManageForm
     */
    void manageSubscribe(SubscribeManageForm subscribeManageForm);

    /**
     * 获取订阅列表
     * @param mailAddress
     * @return
     */
    List<Integer> getSubscribeList(String mailAddress);
}
