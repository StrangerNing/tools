package me.znzn.tools.module.blog.service;

import me.znzn.tools.module.blog.entity.po.Category;

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
    List<Category> searchCategory(Category category);

    /**
     * 计数
     * @param name
     * @return
     */
    Integer countCategory(String name);

}
