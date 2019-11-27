package me.znzn.tools.module.dictionary.service;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.module.dictionary.entity.po.Dictionary;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/27
 * @since 1.0
 */
public interface DictionaryService {

    /**
     * 更新全局变量值
     */
    void updateAllConstant();

    /**
     * 查询所有全局变量
     * @param dictionary 查询条件
     * @return me.znzn.tools.common.component.ResultPage 全局变量列表
     */
    ResultPage getConstantList(Dictionary dictionary);

    /**
     * 更新值记录
     * @param dictionary 更新项
     * @param userInfoVO 登陆用户
     * @return java.lang.Boolean 更新结果
     */
    Boolean updateConstant(Dictionary dictionary, UserInfoVO userInfoVO);

    /**
     * 增加全局常量
     * @param dictionary 新增项
     * @param userInfoVO 登陆用户
     * @return java.lang.Boolean 新增结果
     */
    Boolean addConstant(Dictionary dictionary, UserInfoVO userInfoVO);
}
