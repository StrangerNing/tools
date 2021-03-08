package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.module.blog.mapper.ArticleCommentMapper;
import me.znzn.tools.module.blog.service.ArticleCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务接口实现
 *
 * @author
 * @version 1.0.0
 * @date 2021/03/07 23:43:35
 * @copyright
 */
@Service("articleCommentService")
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Resource
    private ArticleCommentMapper articleCommentMapper;


}
