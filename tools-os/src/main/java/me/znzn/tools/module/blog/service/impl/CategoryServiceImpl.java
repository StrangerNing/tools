package me.znzn.tools.module.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.common.exception.NotFoundException;
import me.znzn.tools.module.blog.entity.form.CategoryForm;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.entity.vo.CategoryVo;
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
    public List<CategoryVo> searchCategory(CategoryForm category) {
        category.setAccurate(false);
        return categoryMapper.selectCategoryVoByProperty(category);
    }

    @Override
    public Integer countCategory(String name) {
        Category query = new Category();
        query.setName(name);
        return categoryMapper.countByProperty(query);
    }

    @Override
    public Category getOneCategory(Category category) {
        List<Category> categories = categoryMapper.selectByProperty(category);
        if (CollectionUtil.isEmpty(categories)) {
            throw new NotFoundException("没有找到分类");
        }
        return categories.get(0);
    }
}
