package me.znzn.tools.module.api.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.BaseModel;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.api.entity.GenerateShortUrlVO;
import me.znzn.tools.module.api.service.ApiService;
import me.znzn.tools.module.blog.entity.constant.BlogRedisConstant;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.oss.mapper.FileMapper;
import me.znzn.tools.module.oss.service.FileService;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.po.ApiKey;
import me.znzn.tools.module.user.entity.po.User;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.module.user.mapper.ApiKeyMapper;
import me.znzn.tools.module.user.mapper.UserMapper;
import me.znzn.tools.utils.UploadFileUtil;
import me.znzn.tools.utils.ValidatorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/4/21
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    @Resource
    private ApiKeyMapper apiKeyMapper;

    @Resource
    private ShortUrlService shortUrlService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileService fileService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public String getShortUrl(GenerateShortUrlVO shortUrlVO) {
        ValidatorUtil.validate(shortUrlVO);
        ApiKey apiKey = validateAk(shortUrlVO.getAk());
        Long userId = apiKey.getCreateId();
        String url = shortUrlVO.getUrl();
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginUrl(url);
        shortUrl.setStatus(StatusEnum.ENABLE.getIndex());
        shortUrl.setCreateAccount(userId);
        shortUrl.setCreateTime(new Date());
        return shortUrlService.saveUrl(shortUrl);
    }

    @Override
    public String uploadFile(Map<String, String> file) {
        String base64 = file.get("file");
        String filename = file.get("filename");

        String key = file.get("ak");
        String type = file.get("type");
        ApiKey apiKey = validateAk(key);
        Long userId = apiKey.getCreateId();
        User user = userMapper.selectByPrimaryKey(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);

        byte[] data = Base64.decode(base64);
        if (StringUtils.isEmpty(filename)) {
            throw new BusinessException("需要文件名");
        }
        String suffix = filename.substring(filename.lastIndexOf("."));
        InputStream inputStream = new ByteArrayInputStream(data);

        OssFileTypeEnum ossFileTypeEnum = StringUtils.isEmpty(type) ? OssFileTypeEnum.OTHERS : OssFileTypeEnum.valueOf(type);
        String fileName = UploadFileUtil.uploadOSS(inputStream, suffix, userInfoVO, ossFileTypeEnum);
        fileService.insertFile(fileName, ossFileTypeEnum, userInfoVO);
        return CommonConstant.FILE_REQUEST_PREFIX + fileName;
    }

    @Override
    public void asyncIg(Map file) {
        List<Map<String, String>> fileList = (List)file.get("fileList");
        String ak = (String)file.get("ak");
        ApiKey apiKey = validateAk(ak);
        Long userId = apiKey.getCreateId();
        User user = userMapper.selectByPrimaryKey(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);

        File query = new File();
        query.setType(OssFileTypeEnum.INSTAGRAM.getIndex());
        List<FileReturnVo> images = fileMapper.selectByPropertyReturnVO(query);
        Set<String> shortCodes = images.stream().map(BaseModel::getRemark).collect(Collectors.toSet());

        int update = 0;
        if (CollectionUtil.isEmpty(fileList)) {
            log.info("同步ig，接收到0张图片");
            return;
        }
        //插入新增的图片
        for (Map<String, String> image : fileList) {
            String base64 = image.get("file");
            String filename = image.get("filename");
            String shortcode = image.get("shortcode");
            if (shortCodes.remove(shortcode)) {
                continue;
            }
            update++;
            byte[] data = Base64.decode(base64);
            if (StringUtils.isEmpty(filename)) {
                throw new BusinessException("需要文件名");
            }
            String suffix = filename.substring(filename.lastIndexOf("."));
            InputStream inputStream = new ByteArrayInputStream(data);

            String fileName = UploadFileUtil.uploadOSS(inputStream, suffix, userInfoVO, OssFileTypeEnum.INSTAGRAM);

            redisTemplate.delete(BlogRedisConstant.INS_KEY);
            fileService.insertFile(fileName, shortcode, OssFileTypeEnum.INSTAGRAM, userInfoVO);
        }
        //删除旧的图片
        shortCodes.forEach(item -> {
            File del = new File();
            del.setType(OssFileTypeEnum.INSTAGRAM.getIndex());
            del.setRemark(item);
            fileService.delFiles(del, userInfoVO);
        });
        log.info("同步ig，接收到{}张，更新{}张，删除{}张", fileList.size(), update, shortCodes.size());
    }

    private ApiKey validateAk(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new BusinessException("ak不可为空");
        }
        ApiKey query = new ApiKey();
        query.setAk(key);
        List<ApiKey> list = apiKeyMapper.selectByProperty(query);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException("ak错误，请确认后重试");
        }
        if (list.size() > 1) {
            throw new BusinessException("ak配置服务器内部出错，请联系网站管理员");
        }
        return list.get(0);
    }
}
