package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.enums.*;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.*;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.*;
import me.znzn.tools.module.blog.service.ArticleService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.UploadFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 服务接口实现
 *
 * @author
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 * @copyright
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private MarkdownMapper markdownMapper;

    @Resource
    private ArticleCategoryMapper articleCategoryMapper;

    @Override
    public Boolean addArticle(ArticleVo articleVo, UserInfoVO loginUser) {
        Article article = new Article();
        BeanUtils.copyProperties(articleVo, article);
        loginUser.setCreateUser(article);
        setDefaultProps(article);
        article.setAuthor(loginUser.getNickname());
        Long result = articleMapper.insertByProperty(article);
        if (!result.equals(1L)) {
            throw new BusinessException("操作失败");
        }
        Long id = article.getId();

        saveArticleTag(articleVo.getTags(), id);

        saveArticleCategory(articleVo.getCategories(), id);

        saveMarkdown(articleVo, id);
        return Boolean.TRUE;
    }

    private void setDefaultProps(Article article) {
        if (article.getComment() == null) {
            article.setComment(CommentLimitEnum.ENABLE.getIndex());
        }
        if (article.getPermission() == null) {
            article.setPermission(ArticlePermissionEnum.ALL.getIndex());
        }
        if (article.getStatus() == null) {
            article.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        }
        if (article.getVersion() == null) {
            article.setVersion(0);
        }
        if (article.getPriority() == null) {
            article.setPriority(ArticlePriorityEnum.NORMAL.getIndex());
        }
        if (article.getViews() == null) {
            article.setViews(0);
        }
    }

    @Override
    public Boolean editArticle(ArticleVo articleVo, UserInfoVO loginUser) {
        Long id = articleVo.getId();
        if (null == id) {
            throw new BusinessException("文章id不能为空");
        }

        Article article = new Article();
        BeanUtils.copyProperties(articleVo, article);
        loginUser.setModifyUser(article);
        Integer result = articleMapper.updateByPrimaryKey(article);
        if (!result.equals(1)) {
            throw new BusinessException("修改失败");
        }

        ArticleTag delete = new ArticleTag();
        delete.setArticleId(id);
        articleTagMapper.deleteByProperty(delete);

        ArticleCategory deleteCategories = new ArticleCategory();
        deleteCategories.setArticleId(id);
        articleCategoryMapper.deleteByProperty(deleteCategories);

        saveArticleTag(articleVo.getTags(), id);

        saveMarkdown(articleVo, id);
        return Boolean.TRUE;
    }

    private void saveArticleTag(List<Tag> tags, Long id) {
        if (CollectionUtil.isEmpty(tags)) {
            return;
        }
        List<ArticleTag> articleTags = new ArrayList<>();
        for (Tag tag : tags) {
            Long tagId = tag.getId();
            if (null == tagId) {
                tagMapper.insertAvoidDuplicate(tag);
                tagId = tag.getId();
            }
            if (null == tagId) {
                List<Tag> query = tagMapper.selectByProperty(tag);
                if (CollectionUtil.isEmpty(query)) {
                    throw new BusinessException("保存tag失败");
                }
                tagId = query.get(0).getId();
            }
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(id);
            articleTag.setTagId(tagId);
            articleTags.add(articleTag);
        }
        if (CollectionUtil.isNotEmpty(articleTags)) {
            articleTagMapper.insertBatchByProperty(articleTags);
        }
    }

    private void saveArticleCategory(List<Category> categories, Long id) {
        if (CollectionUtil.isEmpty(categories)) {
            return;
        }
        Set<Category> duplicate = new HashSet<>(categories);
        List<ArticleCategory> list = new ArrayList<>();
        for (Category category : duplicate) {
            if (category.getId() == null) {
                throw new BusinessException("文章分类出错");
            }
            ArticleCategory articleCategory = new ArticleCategory();
            articleCategory.setArticleId(id);
            articleCategory.setCategoryId(category.getId());
            list.add(articleCategory);
        }
        if (CollectionUtil.isNotEmpty(list)) {
            articleCategoryMapper.insertBatchByProperty(list);
        }
    }

    private void saveMarkdown(ArticleVo articleVo, Long id) {
        if (EditTypeEnum.MARKDOWN.getIndex().equals(articleVo.getEditType())) {
            Markdown markdown = new Markdown();
            markdown.setMarkdown(articleVo.getMarkdown());
            markdown.setId(id);
            markdownMapper.insertAvoidDuplicate(markdown);
        }
    }

    @Override
    public List<ArticleVo> articleList(ArticleForm articleForm, UserInfoVO loginUser) {
        return articleMapper.selectArticleList(articleForm);
    }

    @Override
    public Integer countArticle(ArticleForm articleForm, UserInfoVO loginUser) {
        return articleMapper.countArticle(articleForm);
    }

    @Override
    public ArticleVo articleDetail(Long id) {
        ArticleVo articleVo = articleMapper.selectArticleById(id);
        if (EditTypeEnum.MARKDOWN.getIndex().equals(articleVo.getEditType())) {
            Markdown markdown = markdownMapper.selectByPrimaryKey(id);
            if (markdown != null) {
                articleVo.setMarkdown(markdown.getMarkdown());
            }
        }
        if (StringUtils.isNotEmpty(articleVo.getThumb())) {
            articleVo.setThumbPreview(UploadFileUtil.getFileUrl(articleVo.getThumb(), Long.valueOf(CommonConstant.OSS_URL_EXPIRATION)));
        }
        return articleVo;
    }

    @Override
    public void deleteArticle(Long id) {
        Article query = articleMapper.selectByPrimaryKey(id);
        query.setStatus(ArticleStatusEnum.DELETED.getIndex());
        Integer count = articleMapper.updateByPrimaryKey(query);
        if (!count.equals(1)) {
            throw new BusinessException("删除失败，文章可能已被其他人修改，请刷新后重试");
        }
    }
}
