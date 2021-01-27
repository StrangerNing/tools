package me.znzn.tools.module.oss.service.impl;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.module.oss.entity.form.FileForm;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.oss.mapper.FileMapper;
import me.znzn.tools.module.oss.service.FileService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.UploadFileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务接口实现
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/01/27 11:23:13
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Override
    public void insertFile(String name, OssFileTypeEnum type, UserInfoVO user) {
        File file = new File();
        file.setName(name);
        file.setType(type.getIndex());
        user.setCreateUser(file);
        fileMapper.insertByProperty(file);
    }

    @Override
    public ResultPage getFileList(FileForm file) {
        File f = new File();
        BeanUtils.copyProperties(file, f);
        List<FileReturnVo> list = fileMapper.selectByPropertyReturnVO(f);
        list.forEach(item -> {
            item.setUrl(UploadFileUtil.getFileUrl(item.getName(), Long.valueOf(CommonConstant.OSS_URL_EXPIRATION), file.getStyle()));
        });
        return new ResultPage(fileMapper.countByProperty(f), file.getLimit(), file.getCurrentPage(), list);
    }

    @Override
    public Integer countFileList(File file) {
        return fileMapper.countByProperty(file);
    }

    @Override
    public void delFile(Long id) {
        File file = fileMapper.selectByPrimaryKey(id);

        if (file != null) {
            fileMapper.deleteByPrimaryKey(id);
            UploadFileUtil.delFile(file.getName());
        }
    }

    @Override
    public FileReturnVo getFile(Long id) {
        File file = fileMapper.selectByPrimaryKey(id);
        FileReturnVo fileReturnVo = new FileReturnVo();
        BeanUtils.copyProperties(file, fileReturnVo);
        fileReturnVo.setUrl(UploadFileUtil.getFileUrl(file.getName(), Long.valueOf(CommonConstant.OSS_URL_EXPIRATION)));
        return fileReturnVo;
    }
}
