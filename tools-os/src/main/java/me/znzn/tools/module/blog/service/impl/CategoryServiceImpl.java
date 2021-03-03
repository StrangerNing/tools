package me.znzn.tools.module.blog.service.impl;

import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.mapper.CategoryMapper;
import me.znzn.tools.module.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务接口实现
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/28 21:33:12
 * @copyright
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> searchCategory(Category category) {
        return categoryMapper.selectByProperty(category);
    }

    @Override
    public Integer countCategory(String name) {
        Category query = new Category();
        query.setName(name);
        return categoryMapper.countByProperty(query);
    }
}
