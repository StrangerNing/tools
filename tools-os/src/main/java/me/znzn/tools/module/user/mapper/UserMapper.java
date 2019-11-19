package me.znzn.tools.module.user.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/19 13:09:47
 * @copyright www.ucarinc.com All Rights Reserved.
 */
@Mapper
public interface UserMapper extends BaseDao<User> {

}
