package me.znzn.tools.module.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.enums.SexEnum;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.form.ApiKeyForm;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.po.ApiKey;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.mapper.ApiKeyMapper;
import me.znzn.tools.module.user.mapper.UserMapper;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.MD5Util;
import me.znzn.tools.utils.ValidatorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiKeyMapper apiKeyMapper;

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
//            LOGIN_USER.put(token, userInfo);
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
        newUser.setRoles("[\"user\"]");
        newUser.setUsername(username);
        newUser.setPassword(MD5Util.generate(registerForm.getPassword()));
        newUser.setSex(SexEnum.UNKNOWN.getIndex());
        newUser.setStatus(StatusEnum.ENABLE.getIndex());
        newUser.setCreateTime(new Date());
        return userMapper.insertByProperty(newUser).equals(1L);
    }

    @Override
    public Boolean update(UserInfoVO userInfoVO, UserInfoVO loginUser) {
        User user = new User();
        BeanUtils.copyProperties(userInfoVO, user);
        user.setModifyTime(new Date());
        user.setModifyEmp(loginUser.getId());
        Integer count = userMapper.updateByPrimaryKey(user);
        return count == 1;
    }

    @Override
    public List<ApiKey> getApiKeyList(Long userId) {
        ApiKey query = new ApiKey();
        query.setCreateId(userId);
        return apiKeyMapper.selectByProperty(query);
    }

    @Override
    public ApiKey getApiKey(ApiKeyForm apiKeyForm) {
        ValidatorUtil.validate(apiKeyForm);
        Long userId = apiKeyForm.getCreateId();
        long time = System.currentTimeMillis();
        String code =  String.valueOf(userId) + time;
        String baseCode = Base64Utils.encodeToString(code.getBytes());
        ApiKey apiKey = new ApiKey();
        BeanUtils.copyProperties(apiKeyForm, apiKey);
        apiKey.setAk(baseCode);
        apiKey.setCreateTime(new Date(time));
        return apiKeyMapper.insertByProperty(apiKey) == 1 ? apiKey : null;
    }

    @Override
    public Boolean delApiKey(Long id, Long userId) {
        ApiKey query = apiKeyMapper.selectByPrimaryKey(id);
        if (null == query) {
            throw new BusinessException("该AK不存在");
        }
        if (!userId.equals(query.getCreateId())) {
            throw new BusinessException("您没有权限删除该AK");
        }
        return apiKeyMapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Boolean updateApiKey(ApiKeyForm apiKeyForm, Long userId) {
        if (null == apiKeyForm.getId()) {
            throw new BusinessException("应用编号为空！");
        }
        ApiKey query = apiKeyMapper.selectByPrimaryKey(apiKeyForm.getId());
        if (null == query) {
            throw new BusinessException("该应用不存在");
        }
        if (!query.getCreateId().equals(userId)) {
            throw new BusinessException("您没有权限修改该应用");
        }
        ApiKey apiKey = new ApiKey();
        BeanUtils.copyProperties(apiKeyForm, apiKey);
        apiKey.setModifyId(userId);
        apiKey.setModifyTime(new Date());
        return apiKeyMapper.updateByPrimaryKey(apiKey) == 1;
    }

    @Override
    public List<ApiKey> getApiKeyByKey(String ak, Long userId) {
        ApiKey query = new ApiKey();
        query.setAk(ak);
        query.setCreateId(userId);
        return apiKeyMapper.selectByProperty(query);
    }
}
