package me.znzn.tools;

import cn.hutool.Hutool;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.poi.excel.ExcelFileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.mapper.TagMapper;
import me.znzn.tools.module.blog.service.ArticleService;
import me.znzn.tools.module.blog.service.TagService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.net.URI;
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
//        articleForm.setId(21L);
//        articleForm.setCategory("生活");
        articleForm.setIsSticky(false);
        articleForm.setCurrentPage(1);
        articleForm.setLimit(6);
        articleForm.setTag("test");
        articleForm.setCategory("生活");
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

    @Test
    public void testIgApi() {
        String body = HttpRequest.get("https://instagram40.p.rapidapi.com/account-medias?userid=1719427098&first=10&after=QVFDOGV6dGFtQnJXdnZ0a1FuMkFLSjRHYjdWMEdTTFltMkZpd1FvcUxuQXZ6bDJFVnpKRzFYU3RMSUoyNXluOXFZUVZ3dG1YM3NSTEJqMVI3TTBKM0ZTNg%3D%3D")
                .header("x-rapidapi-key", "f8415df5dcmshbae409d16356056p12ff3bjsnf3b9303894ce")
                .header("x-rapidapi-host", "instagram40.p.rapidapi.com")
                .execute().body();
        System.out.println(body);
    }

    @Test
    public void testSql() {
        ExcelReader excelReader = new ExcelReader("/Users/zhuzening/Downloads/车辆明细.xlsx", 0);
        excelReader.setIgnoreEmptyRow(true);
        List<List<Object>> lists = excelReader.read(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT i.frame_no as '车架号', vo.owner_name AS '现任车主名称', vo.first_owner_name AS '首任车主名称' FROM t_v_ownership vo LEFT JOIN t_v_info i ON vo.vehicle_id = i.id WHERE i.frame_no in (");
        lists.forEach(item -> {
            item.forEach(i -> {
                stringBuilder.append("'").append(i).append("',");
            });
        });
        stringBuilder.append(");");
        System.out.printf(stringBuilder.toString());
    }
}
