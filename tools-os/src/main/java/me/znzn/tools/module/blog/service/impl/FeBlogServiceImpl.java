package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.Page;
import me.znzn.tools.common.component.ResultListData;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.common.exception.NotFoundException;
import me.znzn.tools.module.blog.entity.constant.BlogRedisConstant;
import me.znzn.tools.module.blog.entity.enums.ArticleStatusEnum;
import me.znzn.tools.module.blog.entity.enums.ArticleTypeEnum;
import me.znzn.tools.module.blog.entity.enums.CommentLimitEnum;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.Article;
import me.znzn.tools.module.blog.entity.po.ArticleComment;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleCommentVo;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.ArticleCommentMapper;
import me.znzn.tools.module.blog.mapper.ArticleMapper;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.oss.mapper.FileMapper;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/28
 */
@Slf4j
@Service
public class FeBlogServiceImpl implements FeBlogService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleCommentMapper articleCommentMapper;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private RedisTemplate redisTemplate;


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
        if (null == article || !ArticleStatusEnum.NORMAL.getIndex().equals(article.getStatus())) {
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
    public List<ArticleCommentVo> getLatestComments(ArticleForm articleForm) {
        return articleCommentMapper.selectLatestComments(articleForm);
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

        ArticleVo article = articleMapper.selectArticleById(articleComment.getArticleId());
        if (CommentLimitEnum.DISABLE.getIndex().equals(article.getComment())) {
            throw new BusinessException("当前文章不允许评论");
        }

        articleCommentMapper.insertByProperty(articleComment);
    }

    @Async
    @Override
    public void addViews(Long id, String sourceKey) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        Integer version = (Integer) redisTemplate.opsForValue().get(BlogRedisConstant.VIEW_CACHE_VERSION);

        if (!redisTemplate.opsForValue().setIfAbsent(BlogRedisConstant.VIEW_SOURCE_PREFIX+id+"_"+sourceKey, 1, Long.valueOf(CommonConstant.VALID_ARTICLE_VIEW_INTERVAL), TimeUnit.MILLISECONDS)) {
            log.info("{}已阅读过文章{}", sourceKey, id);
            return;
        }

        redisTemplate.opsForSet().add(BlogRedisConstant.VIEW_CACHE_PREFIX + version, id);
        log.info("{} 阅读文章 {}", sourceKey, id);
        redisTemplate.opsForValue().increment(BlogRedisConstant.VIEW_CACHE_PREFIX + version + "_" + id);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void cacheViewsPersistent() {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        Long version = redisTemplate.opsForValue().increment(BlogRedisConstant.VIEW_CACHE_VERSION) - 1;

        Set<Integer> articleIds = redisTemplate.opsForSet().members(BlogRedisConstant.VIEW_CACHE_PREFIX + version);
        redisTemplate.delete(BlogRedisConstant.VIEW_CACHE_PREFIX + version);
        if (CollectionUtil.isEmpty(articleIds)) {
            return;
        }
        List<Article> articles = new ArrayList<>(articleIds.size());
        List<String> keys = new ArrayList<>();
        for (Integer articleId : articleIds) {
            Integer views = (Integer)redisTemplate.opsForValue().get(BlogRedisConstant.VIEW_CACHE_PREFIX + version + "_" + articleId);
            keys.add(BlogRedisConstant.VIEW_CACHE_PREFIX + version + "_" + articleId);
            Article article = new Article();
            article.setId(articleId.longValue());
            article.setViews(views);
            articles.add(article);
        }
        articleMapper.updateArticleViews(articles);
        redisTemplate.delete(keys);
    }

    @Override
    public void like(Long id, String sourceKey) {

    }

    @Override
    public List<FileReturnVo> getIgImages() {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        List<FileReturnVo> imagesCache = redisTemplate.opsForList().range(BlogRedisConstant.INS_KEY, 0, -1);
        if (CollectionUtil.isNotEmpty(imagesCache)) {
            return imagesCache;
        }
        File query = new File();
        query.setType(OssFileTypeEnum.INSTAGRAM.getIndex());
        List<FileReturnVo> images = fileMapper.selectByPropertyReturnVO(query);
        redisTemplate.opsForList().rightPushAll(BlogRedisConstant.INS_KEY, images);
        return images;
    }
}
