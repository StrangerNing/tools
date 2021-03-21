package me.znzn.tools.module.blog.entity.vo;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.blog.entity.po.Article;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.apache.lucene.document.*;
import org.springframework.util.StreamUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/22
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleVo extends Article implements Serializable {

    private static final long serialVersionUID = -8397539432385057251L;

    private List<Tag> tags;

    private List<CategoryVo> categories;

    private String markdown;

    private String thumbPreview;

    private String aliasHref;

    private UserInfoVO authorInfo;

    public String getThumbPreview() {
        return CommonConstant.FILE_REQUEST_PREFIX + this.getThumb();
    }

    public String getAliasHref() {
        return CommonConstant.ARTICLE_URL_PREFIX + super.getAlias();
    }

    public Document buildDocument() {
        Document document = new Document();
        Field id = new StringField("id", String.valueOf(getId()), Field.Store.YES);
        Field title = new TextField("title", getTitle(), Field.Store.YES);
        Field content = new TextField("content", getContent() == null ? "" : HtmlUtil.cleanHtmlTag(getContent()), Field.Store.YES);
        Field alias = new TextField("alias", super.getAlias(), Field.Store.YES);
        Field author = new StringField("author", getAuthor(), Field.Store.YES);
        Field category = new StoredField("category", JSONArray.toJSONString(categories));
        Field thumb = new StoredField("thumb", getThumb());
        Field createTime = new StoredField("createTime", getCreateTime().getTime());
        Field minutes = new StoredField("minutes", getMinutes());

        document.add(id);
        document.add(content);
        document.add(alias);
        document.add(author);
        document.add(category);
        document.add(title);
        document.add(thumb);
        document.add(createTime);
        document.add(minutes);
        return document;
    }

    public ArticleVo() {

    }

    public ArticleVo(String highlight, Document document) {
        try {
            this.setContent(highlight);
            this.setId(Long.valueOf(document.get("id")));
            this.setAlias(document.get("alias"));
            List list = JSONArray.parseArray(document.get("category"), Category.class);
            this.setCategories(list);
            this.setTitle(document.get("title"));
            this.setThumb(document.get("thumb"));
            this.setCreateTime(new Date(Long.parseLong(document.get("createTime"))));
            this.setMinutes(Integer.valueOf(document.get("minutes")));
        } catch (Exception e) {
            log.error("解析document失败", e);
        }
    }
}
