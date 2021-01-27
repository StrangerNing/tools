package me.znzn.tools.module.oss.mapper;
import me.znzn.tools.common.dao.BaseDao;
import me.znzn.tools.module.oss.entity.po.File;
import java.util.List;

import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/01/27 11:23:13
 */
@Mapper
public interface FileMapper extends BaseDao<File> {

    /**
     * 根据条件获取集合
     * @param file 条件
     * @return List
     */
    List<FileReturnVo> selectByPropertyReturnVO(File file);
}
