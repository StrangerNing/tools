package me.znzn.tools.module.user.service.impl;

import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.enums.SexEnum;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.mapper.UserMapper;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static me.znzn.tools.common.constant.LoginMap.LOGIN_USER;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserInfoVO login(LoginForm loginForm) {
        String username = loginForm.getUsername();
        List<User> userList = userMapper.selectByProperty(User.builder().username(username).build());
        if (ListUtils.isEmpty(userList)) {
            throw new BusinessException("没有这个用户");
        }
        User user = userList.get(0);
        if (MD5Util.verify(loginForm.getPassword(), user.getPassword())) {
            UserInfoVO userInfo = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfo, "password");
            String token = UUID.randomUUID().toString();
            userInfo.setToken(token);
            LOGIN_USER.put(token, userInfo);
            return userInfo;
        }
        throw new BusinessException("用户名或密码错误");
    }

    @Override
    public Boolean register(RegisterForm registerForm) {
        String username = registerForm.getUsername();
        List<User> query = userMapper.selectByProperty(User.builder().username(username).build());
        if (!ListUtils.isEmpty(query)) {
            throw new BusinessException("用户名已被占用");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(MD5Util.generate(registerForm.getPassword()));
        newUser.setSex(SexEnum.UNKNOWN.getIndex());
        newUser.setStatus(StatusEnum.ENABLE.getIndex());
        newUser.setCreateTime(new Date());
        return userMapper.insertByProperty(newUser).equals(1L);
    }
}
