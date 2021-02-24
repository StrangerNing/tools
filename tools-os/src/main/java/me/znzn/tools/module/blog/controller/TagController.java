package me.znzn.tools.module.blog.controller;



import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/02/22 15:22:20
 */
@Controller
@RequestMapping("/blog/tag/")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/search")
    public ResponseEntity searchTag(String tag) {
        return ResultPageUtil.success(tagService.searchTag(tag));
    }

}
