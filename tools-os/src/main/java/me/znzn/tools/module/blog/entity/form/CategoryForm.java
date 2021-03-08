package me.znzn.tools.module.blog.entity.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.module.blog.entity.po.Category;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryForm extends Category {

    private Boolean accurate;
}
