package me.znzn.tools.module.dictionary.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.dictionary.entity.po.Dictionary;
import java.util.List;

import me.znzn.tools.module.dictionary.entity.vo.DictionaryVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典(Dictionary)表数据库访问层
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/27 11:08:36
 */
@Mapper
public interface DictionaryMapper extends BaseDao<Dictionary> {

    /**
     * 获取全局变量列表
     * @param dictionary 查询条件
     * @return 全局变量列表
     */
    List<DictionaryVO> selectConstantListByCondition(Dictionary dictionary);

    /**
     * 更新变量值
     * @param dictionary 更新项
     * @return 更新条数
     */
    Integer updateByCondition(Dictionary dictionary);
}
