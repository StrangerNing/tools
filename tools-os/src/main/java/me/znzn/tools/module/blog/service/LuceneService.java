package me.znzn.tools.module.blog.service;

import me.znzn.tools.common.component.ResultListData;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/21
 */
public interface LuceneService {

    /**
     * 新建索引库
     */
    void createIndex();

    /**
     * 增加文章索引
     * @param articleVo
     */
    void addDocument(ArticleVo articleVo);

    /**
     * 修改文章索引
     * @param articleVo
     */
    void updateDocument(ArticleVo articleVo);

    /**
     * 删除文章索引
     * @param articleVo
     */
    void deleteDocument(ArticleVo articleVo);

    /**
     * 搜索
     * @param articleForm
     * @return
     */
    ResultListData<List<ArticleVo>> search(ArticleForm articleForm);

}
