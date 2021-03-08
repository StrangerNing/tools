package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.form.CategoryForm;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.module.blog.entity.vo.CategoryVo;

import java.util.List;

/**
 * 服务接口
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/28 21:33:12
 */
public interface CategoryService {

    /**
     * 搜索分类
     * @param category
     * @return
     */
    List<CategoryVo> searchCategory(CategoryForm category);

    /**
     * 计数
     * @param name
     * @return
     */
    Integer countCategory(String name);

    /**
     * 获取一个分类
     * @param category
     * @return
     */
    Category getOneCategory(Category category);

}
