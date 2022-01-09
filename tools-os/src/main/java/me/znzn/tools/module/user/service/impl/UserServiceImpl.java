package me.znzn.tools.module.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.MailSendParams;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.enums.SexEnum;
import me.znzn.tools.module.user.entity.enums.UserStatusEnum;
import me.znzn.tools.module.user.entity.form.ApiKeyForm;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.po.ApiKey;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.mapper.ApiKeyMapper;
import me.znzn.tools.module.user.mapper.UserMapper;
import me.znzn.tools.module.user.service.UserService;
import me.znzn.tools.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MailSenderUtil mailSenderUtil;

    @Override
    public UserInfoVO login(LoginForm loginForm) {
        String username = loginForm.getUsername();
        List<User> userList = userMapper.selectByProperty(User.builder().username(username).build());
        if (ListUtils.isEmpty(userList)) {
            throw new BusinessException("没有这个用户");
        }
        User user = userList.get(0);
        if (!MD5Util.verify(loginForm.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (UserStatusEnum.WAIT_AUTH.getIndex().equals(user.getStatus())) {
            throw new BusinessException("请前往邮箱确认激活账号");
        }
        if (UserStatusEnum.DISABLE.getIndex().equals(user.getStatus())) {
            throw new BusinessException("账号已冻结");
        }
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo, "password");
        String token = UUID.randomUUID().toString();
        userInfo.setToken(token);
        userInfo.setViaGoogle(0);
        LoginUserUtil.login(userInfo);
        return userInfo;
    }

    @Override
    public UserInfoVO googleLogin(String googleId) {
        List<User> userList = userMapper.selectByProperty(User.builder().googleId(googleId).build());
        if (ListUtils.isEmpty(userList)) {
            return null;
        }
        User user = userList.get(0);
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo, "password");

        if (UserStatusEnum.DISABLE.getIndex().equals(user.getStatus())) {
            userInfo.setStatus(UserStatusEnum.ENABLE.getIndex());
            update(userInfo);
        }
        String token = UUID.randomUUID().toString();
        userInfo.setToken(token);
        userInfo.setViaGoogle(1);
        LoginUserUtil.login(userInfo);
        return userInfo;
    }

    @Override
    public Boolean register(RegisterForm registerForm) {
        String username = registerForm.getUsername();
        String email = registerForm.getEmail();
        List<User> query = userMapper.selectByProperty(User.builder().username(username).build());
        if (!ListUtils.isEmpty(query)) {
            throw new BusinessException("用户名已被占用");
        }
        if (StringUtils.isNotEmpty(email)) {
            List<User> queryEmail = userMapper.selectByProperty(User.builder().email(registerForm.getEmail()).build());
            if (!ListUtils.isEmpty(queryEmail)) {
                throw new BusinessException("该邮箱已被绑定");
            }
        }
        User newUser = new User();
        newUser.setRoles("[\"user\"]");
        newUser.setUsername(username);
        newUser.setNickname(username);
        newUser.setPassword(MD5Util.generate(registerForm.getPassword()));
        newUser.setSex(SexEnum.UNKNOWN.getIndex());
        newUser.setEmail(registerForm.getEmail());
        newUser.setStatus(UserStatusEnum.WAIT_AUTH.getIndex());
        newUser.setCreateTime(new Date());
        userMapper.insertByProperty(newUser);

        MailSendParams mailSendParams = new MailSendParams();
        mailSendParams.setTo(email);
        mailSendParams.setHref(CommonConstant.SSO_URL + "confirm?eid=");
        mailSenderUtil.send(mailSendParams, MailSenderUtil.MailTypeEnum.USER_EMAIL_CONFIRM);
        return Boolean.TRUE;
    }

    @Override
    public UserInfoVO googleRegister(GoogleIdTokenVerifyUtil.GoogleUser googleUser) {
        List<User> userList = userMapper.selectByProperty(User.builder().email(googleUser.getEmail()).build());
        User user;
        if (CollectionUtil.isNotEmpty(userList)) {
            user = userList.get(0);
            user.setGoogleId(googleUser.getSub());
            user.setModifyTime(new Date());
            user.setModifyEmp(user.getId());
            updateByEmail(user);
        } else {
            user = new User();
            user.setEmail(googleUser.getEmail());
            user.setNickname(googleUser.getName());
            user.setGoogleId(googleUser.getSub());
            user.setSex(SexEnum.UNKNOWN.getIndex());
            user.setCreateTime(new Date());
            user.setRoles("[\"user\"]");
            user.setStatus(UserStatusEnum.ENABLE.getIndex());
            userMapper.insertByProperty(user);
        }
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo, "password");
        String token = UUID.randomUUID().toString();
        userInfo.setToken(token);
        LoginUserUtil.login(userInfo);
        return userInfo;
    }

    @Override
    public Boolean update(UserInfoVO userInfoVO) {
        User user = new User();
        BeanUtils.copyProperties(userInfoVO, user);
        user.setModifyTime(new Date());
        user.setModifyEmp(userInfoVO.getId());
        Integer count = userMapper.updateByPrimaryKey(user);
        return count == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByEmail(User user) {
        if (user.getEmail() == null) {
            throw new BusinessException("用户邮箱为空");
        }
        user.setModifyTime(new Date());
        Integer count = userMapper.updateByEmail(user);
        if (count != 1) {
            throw new BusinessException("更新失败");
        }
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
