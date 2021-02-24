package me.znzn.tools.module.oss.service.impl;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.oss.entity.form.FileForm;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.oss.mapper.FileMapper;
import me.znzn.tools.module.oss.service.FileService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.UploadFileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.znzn.tools.module.url.entity.constant.UrlRedisConstant.FILE_KEY;

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
    @Resource
    private RedisTemplate redisTemplate;

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
    public void delFile(Long id, UserInfoVO user) {
        File file = fileMapper.selectByPrimaryKey(id);

        if (file != null) {
            user.hasOperateAuthThrowException(file);
            fileMapper.deleteByPrimaryKey(id);
            UploadFileUtil.delFile(file.getName());
        }
    }

    @Override
    public void delFile(String name, UserInfoVO user) {
        File file = new File();
        file.setName(name);
        FileReturnVo fileReturnVo = fileMapper.selectOneByProperty(file);

        if (fileReturnVo != null) {
            user.hasOperateAuthThrowException(fileReturnVo);
            fileMapper.deleteByPrimaryKey(fileReturnVo.getId());
            UploadFileUtil.delFile(name);
        }
    }

    @Override
    public FileReturnVo getFile(Long id, UserInfoVO user) {
        FileReturnVo cache = (FileReturnVo) redisTemplate.opsForValue().get(FILE_KEY + id);
        if (cache != null) {
            return cache;
        }
        File file = fileMapper.selectByPrimaryKey(id);
        user.hasOperateAuth(file);
        FileReturnVo fileReturnVo = new FileReturnVo();
        BeanUtils.copyProperties(file, fileReturnVo);
        fileReturnVo.setUrl(UploadFileUtil.getFileUrl(file.getName(), Long.valueOf(CommonConstant.OSS_URL_EXPIRATION)));
        redisTemplate.opsForValue().set(FILE_KEY + id, fileReturnVo, Long.parseLong(CommonConstant.FILE_CACHE_TIME), TimeUnit.HOURS);
        return fileReturnVo;
    }

    @Override
    public FileReturnVo getFile(String name) {
        FileReturnVo cache = (FileReturnVo) redisTemplate.opsForValue().get(FILE_KEY + name);
        if (cache != null) {
            return cache;
        }
        File query = new File();
        query.setName(name);
        FileReturnVo file = fileMapper.selectOneByProperty(query);
        if (file == null) {
            throw new BusinessException("没有找到文件{}", name);
        }
        file.setUrl(UploadFileUtil.getFileUrl(file.getName(), Long.valueOf(CommonConstant.OSS_URL_EXPIRATION)));
        redisTemplate.opsForValue().set(FILE_KEY + name, file, Long.parseLong(CommonConstant.FILE_CACHE_TIME), TimeUnit.HOURS);
        return file;
    }
}
