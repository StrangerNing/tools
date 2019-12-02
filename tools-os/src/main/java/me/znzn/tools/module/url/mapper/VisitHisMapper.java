package me.znzn.tools.module.url.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.url.entity.form.VisitHisForm;
import me.znzn.tools.module.url.entity.po.VisitHis;
import java.util.List;

import me.znzn.tools.module.url.entity.vo.VisitHisVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * (VisitHis)表数据库访问层
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/29 11:19:57
 */
@Mapper
public interface VisitHisMapper extends BaseDao<VisitHis> {

    /**
     * 获取统计信息
     * @param visitHisForm 查询条件
     * @return 结果列表
     */
    List<VisitHisVO> getVisitMapCount(VisitHisForm visitHisForm);
}
