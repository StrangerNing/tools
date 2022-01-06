package me.znzn.tools.module.blog.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.po.Timeline;
import me.znzn.tools.module.blog.service.TimelineService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/2
 */
@RestController
@RequestMapping(("/wapi/blog/timeline"))
public class TimelineController {

    @Resource
    private TimelineService timelineService;

    @GetMapping("/list")
    public ResponseEntity getTimelineList(Timeline timeline) {
        return ResultPageUtil.successWithPage(timelineService.getTimelineList(timeline), timelineService.countTimelineList(timeline), timeline.getCurrentPage());
    }

    @GetMapping("/year")
    public ResponseEntity getTimelineByYear(Timeline timeline) {
        return ResultPageUtil.success(timelineService.getTimelineByYear(timeline));
    }

    @PostMapping("/add")
    public ResponseEntity addEvent(@RequestBody Timeline timeline) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUser();
        if (!userInfoVO.getRoles().contains("admin")) {
            throw new BusinessException("没有操作权限");
        }
        userInfoVO.setCreateUser(timeline);
        timelineService.addEvent(timeline);
        return ResultPageUtil.success();
    }

    @PostMapping("/update")
    public ResponseEntity updateEvent(@RequestBody Timeline timeline) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUser();
        userInfoVO.hasOperateAuthThrowException(timeline);
        userInfoVO.setModifyUser(timeline);
        timelineService.updateEvent(timeline);
        return ResultPageUtil.success();
    }

}
