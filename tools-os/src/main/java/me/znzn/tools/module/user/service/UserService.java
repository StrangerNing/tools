package me.znzn.tools.module.user.service;

import me.znzn.tools.module.user.entity.form.ApiKeyForm;
import me.znzn.tools.module.user.entity.form.LoginForm;
import me.znzn.tools.module.user.entity.form.RegisterForm;
import me.znzn.tools.module.user.entity.po.ApiKey;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */

public interface UserService {

    /**
     * 用户登陆
     * @param loginForm 登陆项
     * @return 用户信息
     */
    UserInfoVO login(LoginForm loginForm);

    /**
     * 用户注册
     * @param registerForm 注册项
     * @return 注册结果
     */
    Boolean register(RegisterForm registerForm);

    /**
     * 更新用户信息
     * @param userInfoVO
     * @return
     */
    Boolean update(UserInfoVO userInfoVO, UserInfoVO loginUser);

    /**
     * 获取用户AK列表
     * @param userId 登陆用户id
     * @return AK列表
     */
    List<ApiKey> getApiKeyList(Long userId);

    /**
     * 获取api key
     * @param apiKeyForm api配置
     * @return api密钥
     */
    ApiKey getApiKey(ApiKeyForm apiKeyForm);

    /**
     * 删除api key
     * @param id api key
     * @param userId 用户id
     * @return 操作结果
     */
    Boolean delApiKey(Long id, Long userId);

    /**
     * 更新api key
     * @param apiKeyForm api配置
     * @param userId 用户id
     * @return 操作结果
     */
    Boolean updateApiKey(ApiKeyForm apiKeyForm, Long userId);

    /**
     * 根据AK查询
     * @param ak api key
     * @param userId 当前登陆用户id
     * @return AK
     */
    List<ApiKey> getApiKeyByKey(String ak, Long userId);
}
