package me.znzn.tools.module.oss.service;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.module.oss.entity.form.FileForm;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;

import java.util.List;

/**
 * 服务接口
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/01/27 11:23:13
 */
public interface FileService {

    /**
     * 插入文件记录
     * @param name 文件名
     * @param type 文件类型
     * @param user 登录用户
     */
    void insertFile(String name, OssFileTypeEnum type, UserInfoVO user);

    /**
     * 获取文件列表
     * @param file 文件
     * @return List
     */
    ResultPage getFileList(FileForm file);

    /**
     * 获取文件个数
     * @param file
     * @return
     */
    Integer countFileList(File file);

    /**
     * 删除文件
     * @param id 文件id
     */
    void delFile(Long id);

    /**
     * 获取文件地址
     * @param id 文件id
     * @return me.znzn.tools.module.oss.entity.vo.FileReturnVo
     */
    FileReturnVo getFile(Long id);
}
