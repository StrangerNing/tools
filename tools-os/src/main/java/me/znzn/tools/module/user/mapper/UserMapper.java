package me.znzn.tools.module.user.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/19 13:09:47
 */
@Mapper
public interface UserMapper extends BaseDao<User> {

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    UserInfoVO selectByUserId(Long id);

    /**
     * 根据邮箱更新用户信息
     * @param user
     * @return
     */
    Integer updateByEmail(User user);

}
