package me.znzn.tools.module.blog.service;

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
     */
    void enableSubscribe(String eid);

    /**
     * 根据eid获取订阅类别
     * @param eid
     * @return
     */
    List<Subscribe> disableSubscribe(String eid);
}
