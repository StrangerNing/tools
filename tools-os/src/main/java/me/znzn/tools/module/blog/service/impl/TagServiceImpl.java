package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.module.blog.mapper.TagMapper;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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


    @Override
    public List<Tag> searchTag(String tag) {
        List<Tag> tagList = tagMapper.searchTag(tag);
        return tagList;
    }

    @Override
    public List<Tag> hotTags(Integer size) {
        return tagMapper.hotTags(size);
    }


}
