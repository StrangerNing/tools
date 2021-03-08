package me.znzn.tools.module.blog.entity.vo;

import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.blog.entity.po.Category;
import me.znzn.tools.utils.UploadFileUtil;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/7
 */
public class CategoryVo extends Category {

    @Override
    public String getHref() {
        if (super.getHref() == null) {
            return null;
        }
        return CommonConstant.CATEGORY_URL_PREFIX + super.getHref();
    }

    @Override
    public String getPic() {
        if (super.getPic() == null) {
            return null;
        }
        return UploadFileUtil.getFileUrl(super.getPic());
    }
}
