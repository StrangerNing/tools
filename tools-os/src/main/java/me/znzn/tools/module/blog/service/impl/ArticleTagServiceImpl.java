package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.module.blog.mapper.ArticleTagMapper;
import me.znzn.tools.module.blog.entity.po.ArticleTag;
import me.znzn.tools.module.blog.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务接口实现
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:23:02
 * @copyright
 */
@Service("articleTagService")
public class ArticleTagServiceImpl implements ArticleTagService {
    @Resource
    private ArticleTagMapper articleTagMapper;

    @Override
    public Long insertByProperty(ArticleTag articleTag) {
        return this.articleTagMapper.insertByProperty(articleTag);
    }

    @Override
    public Integer insertBatchByProperty(List<ArticleTag> list) {
        return this.articleTagMapper.insertBatchByProperty(list);
    }

    @Override
    public Integer updateByPrimaryKey(ArticleTag articleTag) {
        return this.articleTagMapper.updateByPrimaryKey(articleTag);
    }

    @Override
    public Integer updateBatchByPrimaryKey(List<ArticleTag> list) {
        return this.articleTagMapper.updateBatchByPrimaryKey(list);
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return this.articleTagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer deleteBatchByPrimaryKey(List<Long> list) {
        return this.articleTagMapper.deleteBatchByPrimaryKey(list);
    }

    @Override
    public Integer deleteByProperty(ArticleTag articleTag) {
        return this.articleTagMapper.deleteByProperty(articleTag);
    }

    @Override
    public ArticleTag selectByPrimaryKey(Long id) {
        return this.articleTagMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ArticleTag> selectByProperty(ArticleTag articleTag) {
        return this.articleTagMapper.selectByProperty(articleTag);
    }

    @Override
    public Integer countByProperty(ArticleTag articleTag) {
        return this.articleTagMapper.countByProperty(articleTag);
    }
}
