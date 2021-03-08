package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import me.znzn.tools.common.component.BaseModel;
import me.znzn.tools.common.component.Page;
import me.znzn.tools.common.component.ResultListData;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.common.exception.NotFoundException;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.ArticleComment;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleCommentVo;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.ArticleCommentMapper;
import me.znzn.tools.module.blog.mapper.ArticleMapper;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.mapper.UserMapper;
import me.znzn.tools.utils.UploadFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/28
 */
@Service
public class FeBlogServiceImpl<x> implements FeBlogService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleCommentMapper articleCommentMapper;


    @Override
    public List<ArticleVo> getArticleList(ArticleForm articleForm) {
        List<ArticleVo> result = articleMapper.selectArticleList(articleForm);
        return result;
    }

    @Override
    public List<ArticleVo> getRecommendArticle(Long id) {
        ArticleVo articleVo = articleMapper.selectArticleById(id);
        List<Tag> tags = articleVo.getTags();
        if (CollectionUtil.isEmpty(tags)) {
            return null;
        }
        return articleMapper.selectArticleByTags(articleVo);
    }

    @Override
    public Page countArticleList(ArticleForm articleForm) {
        Integer total = articleMapper.countArticle(articleForm);
        return new Page(total, articleForm.getCurrentPage(), articleForm.getLimit());
    }

    @Override
    public ArticleVo getOneArticle(String alias) {
        ArticleVo article = articleMapper.selectArticleByAlias(alias);
        if (null == article) {
            throw new NotFoundException("没有找到文章");
        }
        UserInfoVO author = userMapper.selectByUserId(article.getCreateAccount());
        article.setAuthorInfo(author);
        return article;
    }

    @Override
    public ResultListData getComments(Long articleId) {
        List<ArticleCommentVo> articleComments = articleCommentMapper.selectVoByArticleId(articleId);
        if (CollectionUtil.isEmpty(articleComments)) {
            return new ResultListData(null, 0, 0);
        }
        Map<Long, List<ArticleCommentVo>> groupedComments = articleComments.stream()
                .collect(Collectors.groupingBy(ArticleComment::getParentId));

        List<ArticleCommentVo> root = groupedComments.get(0L);
        if (CollectionUtil.isEmpty(root)) {
            return new ResultListData(null, 0, 0);
        }

        root.forEach(item -> {
            List<ArticleCommentVo> children = groupedComments.get(item.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                item.setChildrenComments(
                        children.stream().sorted(Comparator.comparing(ArticleCommentVo::getCreateTime))
                        .collect(Collectors.toList()));
            }});
        root = root.stream().sorted(Comparator.comparing(ArticleCommentVo::getCreateTime).reversed()).collect(Collectors.toList());
        return new ResultListData(root, articleComments.size(), 1);
    }

    @Override
    public void addArticleComment(ArticleComment articleComment, UserInfoVO loginUser) {
        if (null != loginUser) {
            loginUser.setCreateUser(articleComment);
            articleComment.setCreateName(loginUser.getNickname());
            articleComment.setCreateEmail(loginUser.getEmail());
        } else {
            Date now = new Date();
            articleComment.setCreateTime(now);
            articleComment.setModifyTime(now);
        }

        if (StringUtils.isEmpty(articleComment.getCreateName())) {
            throw new BusinessException("请输入你的名字");
        }
        if (StringUtils.isEmpty(articleComment.getContent())) {
            throw new BusinessException("请输入评论内容");
        }
        if (articleComment.getArticleId() == null) {
            throw new BusinessException("文章id丢失，请重试");
        }
        if (articleComment.getParentId() == null) {
            articleComment.setParentId(0L);
        }

        articleCommentMapper.insertByProperty(articleComment);
    }

    @Async
    @Override
    public void addViews(Long id) {

    }
}
