package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.po.Tag;
import java.util.List;

/**
 * 服务接口
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 */
public interface TagService {

    /**
     * 搜索tag
     * @param tag
     * @return
     */
    List<Tag> searchTag(String tag);

    /**
     * 获取热门标签
     * @param size
     * @return
     */
    List<Tag> hotTags(Integer size);

    /**
     * 重置热门标签缓存
     */
    void clearTagCache();

    /**
     * 获取一个标签
     * @param tag
     * @return
     */
    Tag getOneTag(Tag tag);
}
