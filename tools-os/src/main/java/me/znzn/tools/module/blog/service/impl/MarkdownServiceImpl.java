package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.module.blog.mapper.MarkdownMapper;
import me.znzn.tools.module.blog.entity.po.Markdown;
import me.znzn.tools.module.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务接口实现
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
@Service("markdownService")
public class MarkdownServiceImpl implements MarkdownService {
    @Resource
    private MarkdownMapper markdownMapper;

    @Override
    public Long insertByProperty(Markdown markdown) {
        return this.markdownMapper.insertByProperty(markdown);
    }

    @Override
    public Integer insertBatchByProperty(List<Markdown> list) {
        return this.markdownMapper.insertBatchByProperty(list);
    }

    @Override
    public Integer updateByPrimaryKey(Markdown markdown) {
        return this.markdownMapper.updateByPrimaryKey(markdown);
    }

    @Override
    public Integer updateBatchByPrimaryKey(List<Markdown> list) {
        return this.markdownMapper.updateBatchByPrimaryKey(list);
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return this.markdownMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer deleteBatchByPrimaryKey(List<Long> list) {
        return this.markdownMapper.deleteBatchByPrimaryKey(list);
    }

    @Override
    public Integer deleteByProperty(Markdown markdown) {
        return this.markdownMapper.deleteByProperty(markdown);
    }

    @Override
    public Markdown selectByPrimaryKey(Long id) {
        return this.markdownMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Markdown> selectByProperty(Markdown markdown) {
        return this.markdownMapper.selectByProperty(markdown);
    }

    @Override
    public Integer countByProperty(Markdown markdown) {
        return this.markdownMapper.countByProperty(markdown);
    }
}
