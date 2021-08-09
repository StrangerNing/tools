package me.znzn.tools.module.blog.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.blog.entity.po.Friends;
import me.znzn.tools.module.blog.service.FeBlogService;
import me.znzn.tools.module.blog.service.FriendsService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/6/25
 */
@RestController
@RequestMapping("/wapi/blog/friends")
public class FriendsController {
    @Resource
    private FeBlogService feBlogService;
    @Resource
    private FriendsService friendsService;

    @PostMapping("/apply")
    public ResponseEntity applyFriends(@RequestBody Friends friends, HttpServletRequest request) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUserWithoutThrow();
        String requestId = request.getSession().getId();
        feBlogService.applyFriendsLink(friends, userInfoVO, requestId);
        return ResultPageUtil.success();
    }

    @GetMapping("/list")
    public ResponseEntity list(Friends friends) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUser();
        if (!userInfoVO.getRoles().contains("admin")) {
            friends.setCreateAccount(userInfoVO.getId());
        }
        friends.setOrderBy("modify_time DESC");
        return ResultPageUtil.successWithPage(friendsService.getFriendsList(friends), friendsService.countFriendsList(friends), friends.getCurrentPage());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Friends friends) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUser();
        if (!userInfoVO.getRoles().contains("admin")) {
            throw new BusinessException("没有操作权限");
        }
        userInfoVO.setCreateUser(friends);
        friends.setVersion(0);
        friendsService.addFriendsLink(friends);
        return ResultPageUtil.success();
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Friends friends) {
        UserInfoVO userInfoVO = LoginUserUtil.getSessionUser();
        if (!userInfoVO.getRoles().contains("admin") && !userInfoVO.getId().equals(friends.getCreateAccount())) {
            throw new BusinessException("没有操作权限");
        }
        userInfoVO.setModifyUser(friends);
        friendsService.updateFriendsLink(friends);
        return ResultPageUtil.success();
    }
}
