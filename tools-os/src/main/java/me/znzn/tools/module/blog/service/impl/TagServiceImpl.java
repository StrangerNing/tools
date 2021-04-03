package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;
import me.znzn.tools.common.exception.NotFoundException;
import me.znzn.tools.module.blog.entity.constant.BlogRedisConstant;
import me.znzn.tools.module.blog.mapper.TagMapper;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 服务接口实现
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 */
@Service("tagService")
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;
    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public List<Tag> searchTag(String tag) {
        List<Tag> tagList = tagMapper.searchTag(tag);
        return tagList;
    }

    @Override
    public List<Tag> hotTags(Integer size) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        List<Tag> list = redisTemplate.opsForList().range(BlogRedisConstant.TAGS_KEY, 0, size == null ? -1: size);
        if (CollectionUtil.isNotEmpty(list)) {
            return list;
        }
        list = tagMapper.hotTags(size);
        redisTemplate.opsForList().rightPushAll(BlogRedisConstant.TAGS_KEY, list);
        return list;
    }

    @Override
    public void clearTagCache() {
        redisTemplate.delete(BlogRedisConstant.TAGS_KEY);
    }

    @Override
    public Tag getOneTag(Tag tag) {
        List<Tag> tagList = tagMapper.selectByProperty(tag);
        if (CollectionUtil.isEmpty(tagList)) {
            throw new NotFoundException("没有找到标签");
        }
        return tagList.get(0);
    }


}
