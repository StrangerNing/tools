package me.znzn.tools;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.TagMapper;
import me.znzn.tools.module.blog.service.ArticleService;
import me.znzn.tools.module.blog.service.TagService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/23
 */
@Slf4j
@SpringBootTest
public class BlogTests {
    @Resource
    private ArticleService articleService;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private TagService tagService;

    @Test
    public void testAddTag() {
        Tag tag = new Tag();
        tag.setTag("aaaa");
        tagMapper.insertAvoidDuplicate(tag);
        System.out.println(tag.getId());
    }

    @Test
    public void testSearchTag() {
        String tag = "f";
        List<Tag> tagList = tagService.searchTag(tag);
        if (CollectionUtil.isEmpty(tagList)) {
            log.error("none result");
            return;
        }
        tagList.forEach(item -> log.error(item.getTag()));
    }

    @Test
    public void testArticleList() {
        ArticleForm articleForm = new ArticleForm();
        articleForm.setId(18L);
        List<ArticleVo> articleVos = articleService.articleList(articleForm, new UserInfoVO());
        if (CollectionUtil.isNotEmpty(articleVos)) {
            articleVos.forEach(item -> {
                log.error("文章id：{},文章名：{}",item.getId(), item.getTitle());
                if (CollectionUtil.isNotEmpty(item.getTags())) {
                    item.getTags().forEach(tag -> {
                        log.error("文章标签：{}", tag.getTag());
                    });
                }
            });
        }
    }
}
