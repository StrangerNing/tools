package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.Page;
import me.znzn.tools.common.component.ResultListData;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.enums.ArticleStatusEnum;
import me.znzn.tools.module.blog.entity.form.ArticleForm;
import me.znzn.tools.module.blog.entity.vo.ArticleVo;
import me.znzn.tools.module.blog.service.ArticleService;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.module.blog.service.LuceneService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/21
 */
@Slf4j
@Service
public class LuceneServiceImpl implements LuceneService {

    @Resource
    private Directory directory;
    @Resource
    private Analyzer analyzer;
    @Resource
    private FeBlogService feBlogService;
    @Resource
    private ArticleService articleService;


    @Override
    public void createIndex() {
        ArticleForm articleForm = new ArticleForm();
        articleForm.setStatus(ArticleStatusEnum.NORMAL.getIndex());
        List<ArticleVo> articleList = feBlogService.getArticleList(articleForm);

        List<Document> documentList = new ArrayList<>();
        articleList.forEach(article -> {
            Document document = article.buildDocument();
            documentList.add(document);
        });

        try {

            IndexWriterConfig cfg = new IndexWriterConfig(analyzer);

            IndexWriter indexWriter = new IndexWriter(directory, cfg);

            indexWriter.deleteAll();
            indexWriter.addDocuments(documentList);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException io) {
            log.error("创建索引库失败，IO异常", io);
        }
    }

    @Override
    public void addDocument(ArticleVo articleVo) {
        try {
            Document document = articleVo.buildDocument();
            IndexWriterConfig cfg = new IndexWriterConfig(analyzer);

            IndexWriter indexWriter = new IndexWriter(directory, cfg);
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException io) {
            log.error("增加索引失败，IO异常", io);
        }
    }

    @Override
    public void updateDocument(ArticleVo articleVo) {
        IndexWriter indexWriter = null;
        try {
            Long articleId = articleVo.getId();
            if (articleId == null) {
                throw new BusinessException("文章id缺失");
            }

            IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, cfg);

            Document document = articleVo.buildDocument();
            indexWriter.updateDocument(new Term("id", String.valueOf(articleVo.getId())), document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException io) {
            log.error("更新文章索引失败，IO异常", io);
        }
    }

    @Override
    public void deleteDocument(ArticleVo articleVo) {
        try {
            IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, cfg);
            indexWriter.deleteDocuments(new Term("id", String.valueOf(articleVo.getId())));
        } catch (IOException io) {
            log.error("删除文章索引失败，IO异常", io);
        }
    }

    @Override
    public ResultListData<List<ArticleVo>> search(ArticleForm articleForm) {
        try {
            DirectoryReader open = DirectoryReader.open(directory);
            //创建IndexSearcher对象
            IndexSearcher indexSearcher = new IndexSearcher(open);

            //创建QueryParser对象
            QueryParser queryParser = new MultiFieldQueryParser(new String[]{"content","title","alias"}, analyzer);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);

            //给出要查询的关键字
            String keyWords = articleForm.getContent();

            //创建Query对象来封装关键字
            Query query = queryParser.parse(keyWords);

            //用IndexSearcher对象去索引库中查询符合条件的前100条记录，不足100条记录的以实际为准
            TopDocs topDocs = indexSearcher.search(query, articleForm.getCurrentPage() * articleForm.getLimit());

            //获取符合条件的编号

            Formatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
            Scorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(formatter,scorer);

            //设置摘要
            Fragmenter fragmenter  = new SimpleFragmenter(100);
            highlighter.setTextFragmenter(fragmenter);

            List<ArticleVo> articleList = new ArrayList<>(articleForm.getLimit());
            for(int i=articleForm.getStartIndex();(i < articleForm.getCurrentPage() * articleForm.getLimit()) && i<topDocs.scoreDocs.length;i++){
                ScoreDoc scoreDoc = topDocs.scoreDocs[i];
                int no = scoreDoc.doc;
                Document document = indexSearcher.doc(no);

                String highlighterContent = highlighter.getBestFragment(analyzer,"content",document.get("content"));

                ArticleVo article = new ArticleVo(highlighterContent, document);
                articleList.add(article);
            }
            return new ResultListData<List<ArticleVo>>(articleList, (int)topDocs.totalHits.value, articleForm.getCurrentPage());
        } catch (IOException io) {
            log.error("查询失败，IO异常", io);
        } catch (InvalidTokenOffsetsException ito) {
            log.error("查询失败，高亮化文章内容异常", ito);
        } catch (ParseException p) {
            log.error("查询失败，解析关键字异常", p);
        }
        return new ResultListData<>(null, 0, 0);
    }
}
