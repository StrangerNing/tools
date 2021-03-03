package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.module.blog.mapper.ArticleCategoryMapper;
import me.znzn.tools.module.blog.service.ArticleCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务接口实现
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/28 21:33:36
 */
@Service("articleCategoryService")
public class ArticleCategoryServiceImpl implements ArticleCategoryService {
    @Resource
    private ArticleCategoryMapper articleCategoryMapper;


}
