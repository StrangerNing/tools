package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.ArticleMapper;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.utils.UploadFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/28
 */
@Service
public class FeBlogServiceImpl implements FeBlogService {

    @Resource
    private ArticleMapper articleMapper;


    @Override
    public List<ArticleVo> getArticleList(ArticleForm articleForm) {
        List<ArticleVo> result = articleMapper.selectArticleList(articleForm);
        result.forEach(article -> {
            if (StringUtils.isNotEmpty(article.getThumb())) {
                article.setThumbPreview(UploadFileUtil.getFileUrl(article.getThumb()));
            }
        });
        return result;
    }

    @Override
    public Integer countArticleList(ArticleForm articleForm) {
        return articleMapper.countArticle(articleForm);
    }
}
