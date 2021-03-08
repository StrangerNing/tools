package me.znzn.tools.module.blog.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.blog.entity.po.Article;
import me.znzn.tools.module.blog.entity.po.Tag;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.UploadFileUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/2/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleVo extends Article implements Serializable {

    private static final long serialVersionUID = -8397539432385057251L;

    private List<Tag> tags;

    private List<CategoryVo> categories;

    private String markdown;

    private String thumbPreview;

    private UserInfoVO authorInfo;

    public String getThumbPreview() {
        return UploadFileUtil.getFileUrl(this.getThumb());
    }

    @Override
    public String getAlias() {
        return CommonConstant.ARTICLE_URL_PREFIX + super.getAlias();
    }
}
