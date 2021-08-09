package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.po.Friends;

import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/6/25
 */
public interface FriendsService {

    /**
     * 获取友链
     * @param friends
     * @return
     */
    List<Friends> getFriendsList(Friends friends);

    /**
     * 获取友链个数
     * @param friends
     * @return
     */
    Integer countFriendsList(Friends friends);

    /**
     * 新增友链
     * @param friends
     */
    void addFriendsLink(Friends friends);

    /**
     * 修改友链
     * @param friends
     */
    void updateFriendsLink(Friends friends);
}
